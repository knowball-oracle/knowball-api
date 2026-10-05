package br.com.fiap.knowball.service;

import br.com.fiap.knowball.model.AnalysisResultType;
import br.com.fiap.knowball.model.Report;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.HtmlUtils;

import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

@Service
@Slf4j
public class EmailService {

    @Value("${brevo.api.key}")
    private String apiKey;

    @Value("${brevo.sender.email}")
    private String senderEmail;

    @Value("${brevo.sender.name}")
    private String senderName;

    private static final String BREVO_URL = "https://api.brevo.com/v3/smtp/email";

    private static final DateTimeFormatter FORMATTER =
            DateTimeFormatter.ofPattern("dd/MM/yyyy 'as' HH:mm")
                    .withZone(ZoneId.of("America/Sao_Paulo"));

    private final RestTemplate restTemplate = new RestTemplate();

    @Async
    public void sendReportConfirmation(Report report) {
        try {
            sendViaBrevo(report.getUser().getEmail(),
                    "Denuncia recebida - Protocolo " + report.getProtocol(),
                    buildHtml(report));

            log.info("E-mail enviado via Brevo para {} - protocolo {}",
                    report.getUser().getEmail(), report.getProtocol());

        } catch (HttpClientErrorException e) {
            log.error("Erro Brevo [{}] para protocolo {}: {}",
                    e.getStatusCode(), report.getProtocol(), e.getResponseBodyAsString());
        } catch (Exception e) {
            log.error("Falha ao enviar e-mail para protocolo {}: {}",
                    report.getProtocol(), e.getMessage(), e);
        }
    }


    public void sendUnderReviewEmail(Report report) {
        ReportMailData data = ReportMailData.from(report);

        String intro = """
            Informamos que o seu relato est&aacute; <strong style="color:#e8af34;">em an&aacute;lise</strong>
            pela nossa equipe. Estamos avaliando as informa&ccedil;&otilde;es enviadas e voc&ecirc; ser&aacute;
            avisado por e-mail assim que houver uma conclus&atilde;o.
            """;

        String html = buildStatusHtml(data, "Em an&aacute;lise", "#e8af34", intro, "");
        dispatchStatusEmail(data, "Denuncia em analise - Protocolo " + data.protocol(), html);
    }

    public void sendResolvedEmail(Report report) {
        ReportMailData data = ReportMailData.from(report);

        String intro = """
            A sua den&uacute;ncia foi <strong style="color:#6daa45;">resolvida</strong>.
            Nossa equipe concluiu a an&aacute;lise do relato abaixo. Agradecemos por
            contribuir com a integridade do futebol de base.
            """;

        String html = buildStatusHtml(
                data, "Resolvida", "#6daa45", intro, buildAnalysisRow(report.getAnalysisResult()));
        dispatchStatusEmail(data, "Denuncia resolvida - Protocolo " + data.protocol(), html);
    }

    public void sendVerificationCode(String toEmail, String subject, String htmlContent) {
        try {
            sendViaBrevo(toEmail, subject, htmlContent);
            log.info("Código de verificação enviado via Brevo para {}", toEmail);
        } catch (HttpClientErrorException e) {
            log.error("Erro Brevo [{}] ao enviar código para {}: {}",
                    e.getStatusCode(), toEmail, e.getResponseBodyAsString());
            throw new IllegalStateException("Não foi possível enviar o código de verificação.", e);
        } catch (Exception e) {
            log.error("Falha ao enviar código de verificação para {}: {}", toEmail, e.getMessage(), e);
            throw new IllegalStateException("Não foi possível enviar o código de verificação.", e);
        }
    }

    private void sendViaBrevo(String toEmail, String subject, String htmlContent) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("api-key", apiKey);

        Map<String, Object> body = Map.of(
                "sender", Map.of(
                        "name", senderName,
                        "email", senderEmail
                ),
                "to", List.of(
                        Map.of("email", toEmail)
                ),
                "subject", subject,
                "htmlContent", htmlContent
        );

        HttpEntity<Map<String, Object>> request = new HttpEntity<>(body, headers);
        restTemplate.postForEntity(BREVO_URL, request, String.class);
    }

    private record ReportMailData(String to, String userName, String protocol, String content) {
        static ReportMailData from(Report report) {
            var user = report.getUser();
            String name = (user.getName() != null && !user.getName().isBlank())
                    ? user.getName()
                    : user.getEmail();
            return new ReportMailData(user.getEmail(), name, report.getProtocol(), report.getContent());
        }
    }

    private void dispatchStatusEmail(ReportMailData data, String subject, String html) {
        CompletableFuture.runAsync(() -> {
            try {
                sendViaBrevo(data.to(), subject, html);
                log.info("E-mail '{}' enviado via Brevo para {}", subject, data.to());
            } catch (HttpClientErrorException e) {
                log.error("Erro Brevo [{}] para protocolo {}: {}",
                        e.getStatusCode(), data.protocol(), e.getResponseBodyAsString());
            } catch (Exception e) {
                log.error("Falha ao enviar e-mail para protocolo {}: {}",
                        data.protocol(), e.getMessage(), e);
            }
        });
    }

    private static String esc(String value) {
        return value == null ? "" : HtmlUtils.htmlEscape(value);
    }

    private String buildAnalysisRow(AnalysisResultType result) {
        if (result == null) {
            return "";
        }

        String label;
        String color;
        String text;

        switch (result) {
            case POSITIVE -> {
                label = "Positivo";
                color = "#6daa45";
                text = "O tom do seu relato foi classificado como positivo. Isso indica uma "
                        + "contribui&ccedil;&atilde;o construtiva e colaborativa, que ajuda a manter "
                        + "o futebol de base &iacute;ntegro.";
            }
            case NEUTRAL -> {
                label = "Neutro";
                color = "#a9a8a4";
                text = "O tom do seu relato foi classificado como neutro. Sua descri&ccedil;&atilde;o "
                        + "foi objetiva e factual, o que facilita o trabalho de an&aacute;lise da "
                        + "nossa equipe.";
            }
            case NEGATIVE -> {
                label = "Negativo";
                color = "#dd6974";
                text = "O tom do seu relato foi classificado como negativo. Entendemos que a "
                        + "situa&ccedil;&atilde;o relatada &eacute; s&eacute;ria e preocupante, e por "
                        + "isso levamos o caso a s&eacute;rio durante toda a an&aacute;lise.";
            }
            default -> {
                return "";
            }
        }

        return """
            <tr>
            <td style="padding:24px 40px 0;">
            <p style="margin:0 0 10px;color:#797876;font-size:11px;text-transform:uppercase;
            letter-spacing:1.5px;font-weight:600;">Sentimento do relato</p>
            <div style="background:#141312;border:1px solid #2a2927;border-left:3px solid %1$s;
            border-radius:0 10px 10px 0;padding:16px 20px;">
            <p style="margin:0;color:%1$s;font-size:18px;font-weight:700;">%2$s</p>
            <p style="margin:8px 0 0;color:#cdccca;font-size:14px;line-height:1.7;">%3$s</p>
            </div>
            </td>
            </tr>
            """.formatted(color, label, text);
    }

    private String buildStatusHtml(ReportMailData data, String statusLabel, String statusColor,
                                   String introHtml, String extraRows) {
        return """
            <!DOCTYPE html>
            <html lang="pt-BR">
            <head>
            <meta charset="UTF-8"/>
            <meta name="viewport" content="width=device-width, initial-scale=1.0"/>
            </head>
            <body style="margin:0;padding:0;background:#0f0e0c;font-family:'Segoe UI',Arial,sans-serif;">
            <table width="100%%" cellpadding="0" cellspacing="0" style="background:#0f0e0c;padding:40px 0;">
            <tr>
            <td align="center">
            <table width="600" cellpadding="0" cellspacing="0"
            style="background:#1c1b19;border-radius:16px;overflow:hidden;border:1px solid #2a2927;">
            <tr>
            <td style="background:linear-gradient(135deg,#01696f,#0c4e54);padding:32px 40px;text-align:center;">
            <h1 style="margin:0;color:#ffffff;font-size:28px;font-weight:700;letter-spacing:-0.5px;">
            Knowball
            </h1>
            <p style="margin:6px 0 0;color:#7cc4c9;font-size:13px;font-weight:600;letter-spacing:1px;text-transform:uppercase;">
            Sistema de Denuncias - Categorias de Base do Futebol Brasileiro Masculino
            </p>
            </td>
            </tr>
            <tr>
            <td style="padding:32px 40px 0;text-align:center;">
            <div style="display:inline-block;background:#0f0e0c;border:1px solid #01696f;
            border-radius:12px;padding:16px 32px;">
            <p style="margin:0;color:#4f98a3;font-size:11px;text-transform:uppercase;
            letter-spacing:2px;font-weight:600;">Protocolo</p>
            <p style="margin:8px 0 0;color:#ffffff;font-size:28px;font-weight:700;
            letter-spacing:2px;font-family:monospace;">%1$s</p>
            </div>
            <div style="margin-top:16px;">
            <span style="display:inline-block;background:#141312;border:1px solid %4$s;color:%4$s;
            border-radius:999px;padding:6px 14px;font-size:12px;font-weight:700;
            letter-spacing:1px;text-transform:uppercase;">%3$s</span>
            </div>
            </td>
            </tr>
            <tr>
            <td style="padding:28px 40px 0;">
            <p style="margin:0;color:#cdccca;font-size:15px;line-height:1.6;">
            Ol&aacute;, <strong style="color:#ffffff;">%2$s</strong>!
            </p>
            <p style="margin:12px 0 0;color:#cdccca;font-size:15px;line-height:1.6;">
            %5$s
            </p>
            </td>
            </tr>
            <tr>
            <td style="padding:24px 40px 0;">
            <p style="margin:0 0 10px;color:#797876;font-size:11px;text-transform:uppercase;
            letter-spacing:1.5px;font-weight:600;">Relato</p>
            <div style="background:#141312;border-left:3px solid #01696f;
            border-radius:0 8px 8px 0;padding:16px 20px;">
            <p style="margin:0;color:#cdccca;font-size:14px;line-height:1.7;
            white-space:pre-wrap;">%6$s</p>
            </div>
            </td>
            </tr>
            %7$s
            <tr>
            <td style="padding:32px 40px;text-align:center;border-top:1px solid #2a2927;">
            <p style="margin:24px 0 0;color:#5a5957;font-size:12px;line-height:1.6;">
            Este e-mail foi enviado automaticamente pelo sistema Knowball.<br/>
            Knowball - Integridade no Futebol de Base
            </p>
            </td>
            </tr>
            </table>
            </td>
            </tr>
            </table>
            </body>
            </html>
            """.formatted(
                esc(data.protocol()),
                esc(data.userName()),
                statusLabel,
                statusColor,
                introHtml,
                esc(data.content()),
                extraRows
        );
    }

    private String buildHtml(Report report) {
        String dataFormatada = FORMATTER.format(report.getDate());
        String userName = report.getUser().getName() != null
                ? report.getUser().getName()
                : report.getUser().getEmail();

        return """
            <!DOCTYPE html>
            <html lang="pt-BR">
            <head>
            <meta charset="UTF-8"/>
            <meta name="viewport" content="width=device-width, initial-scale=1.0"/>
            </head>
            <body style="margin:0;padding:0;background:#0f0e0c;font-family:'Segoe UI',Arial,sans-serif;">
            <table width="100%%" cellpadding="0" cellspacing="0" style="background:#0f0e0c;padding:40px 0;">
            <tr>
            <td align="center">
            <table width="600" cellpadding="0" cellspacing="0"
            style="background:#1c1b19;border-radius:16px;overflow:hidden;border:1px solid #2a2927;">
            <tr>
            <td style="background:linear-gradient(135deg,#01696f,#0c4e54);padding:32px 40px;text-align:center;">
            <h1 style="margin:0;color:#ffffff;font-size:28px;font-weight:700;letter-spacing:-0.5px;">
            Knowball
            </h1>
            <p style="margin:6px 0 0;color:#7cc4c9;font-size:13px;font-weight:600;letter-spacing:1px;text-transform:uppercase;">
                    Sistema de Denuncias - Categorias de Base do Futebol Brasileiro Masculino
            </p>
            </td>
            </tr>
            <tr>
            <td style="padding:32px 40px 0;text-align:center;">
            <div style="display:inline-block;background:#0f0e0c;border:1px solid #01696f;
            border-radius:12px;padding:16px 32px;">
            <p style="margin:0;color:#4f98a3;font-size:11px;text-transform:uppercase;
            letter-spacing:2px;font-weight:600;">Protocolo gerado</p>
            <p style="margin:8px 0 0;color:#ffffff;font-size:28px;font-weight:700;
            letter-spacing:2px;font-family:monospace;">%s</p>
            </div>
            </td>
            </tr>
            <tr>
            <td style="padding:28px 40px 0;">
            <p style="margin:0;color:#cdccca;font-size:15px;line-height:1.6;">
            Ola, <strong style="color:#ffffff;">%s</strong>!
            </p>
            <p style="margin:12px 0 0;color:#cdccca;font-size:15px;line-height:1.6;">
            Sua denuncia foi <strong style="color:#4f98a3;">recebida com sucesso</strong>
            em <strong>%s</strong> e ja esta na fila de analise da nossa equipe.
            </p>
            </td>
            </tr>
            <tr>
            <td style="padding:24px 40px 0;">
            <p style="margin:0 0 10px;color:#797876;font-size:11px;text-transform:uppercase;
            letter-spacing:1.5px;font-weight:600;">Seu relato</p>
            <div style="background:#141312;border-left:3px solid #01696f;
            border-radius:0 8px 8px 0;padding:16px 20px;">
            <p style="margin:0;color:#cdccca;font-size:14px;line-height:1.7;
            white-space:pre-wrap;">%s</p>
            </div>
            </td>
            </tr>
            <tr>
            <td style="padding:24px 40px 0;">
            <div style="background:#1a1918;border:1px solid #2a2927;border-radius:10px;padding:16px 20px;">
            <p style="margin:0;color:#797876;font-size:13px;line-height:1.6;">
            <strong style="color:#cdccca;">Confidencialidade garantida.</strong>
            Suas informacoes sao protegidas e utilizadas exclusivamente para a analise desta denuncia.
            </p>
            </div>
            </td>
            </tr>
            <tr>
            <td style="padding:32px 40px;text-align:center;border-top:1px solid #2a2927;">
            <p style="margin:0;color:#5a5957;font-size:12px;line-height:1.6;">
            Este e-mail foi enviado automaticamente pelo sistema Knowball.
            
            Knowball - Integridade no Futebol de Base
            </p>
            </td>
            </tr>
            </table>
            </td>
            </tr>
            </table>
            </body>
            </html>
        """
                .formatted(
                        report.getProtocol(),
                        userName,
                        dataFormatada,
                        report.getContent()
                );
    }
}