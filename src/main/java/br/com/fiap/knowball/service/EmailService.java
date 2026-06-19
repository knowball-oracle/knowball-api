package br.com.fiap.knowball.service;

import br.com.fiap.knowball.model.Report;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;

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
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            headers.set("api-key", apiKey);

            Map<String, Object> body = Map.of(
                    "sender", Map.of(
                            "name", senderName,
                            "email", senderEmail
                    ),
                    "to", List.of(
                            Map.of("email", report.getUser().getEmail())
                    ),
                    "subject", "Denuncia recebida - Protocolo " + report.getProtocol(),
                    "htmlContent", buildHtml(report)
            );

            HttpEntity<Map<String, Object>> request = new HttpEntity<>(body, headers);
            ResponseEntity<String> response = restTemplate.postForEntity(BREVO_URL, request, String.class);

            log.info("E-mail enviado via Brevo para {} - protocolo {} - status: {}",
                    report.getUser().getEmail(), report.getProtocol(), response.getStatusCode());

        } catch (HttpClientErrorException e) {
            log.error("Erro Brevo [{}] para protocolo {}: {}",
                    e.getStatusCode(), report.getProtocol(), e.getResponseBodyAsString());
        } catch (Exception e) {
            log.error("Falha ao enviar e-mail para protocolo {}: {}",
                    report.getProtocol(), e.getMessage(), e);
        }
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
                          <p style="margin:6px 0 0;color:#a8d5d7;font-size:13px;letter-spacing:1px;text-transform:uppercase;">
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
                report.getProtocol(),
                userName,
                dataFormatada,
                report.getContent()
        );
    }

}