package br.com.fiap.knowball.service;

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

    private static String esc(String value) {
        return value == null ? "" : HtmlUtils.htmlEscape(value);
    }

    private String buildHtml(Report report) {
        String dataFormatada = FORMATTER.format(report.getDate());
        String userName = (report.getUser().getName() != null && !report.getUser().getName().isBlank())
                ? report.getUser().getName()
                : report.getUser().getEmail();

        return """
        <!DOCTYPE html>
        <html lang="pt-BR" xmlns="http://www.w3.org/1999/xhtml">
        <head>
          <meta charset="UTF-8"/>
          <meta name="viewport" content="width=device-width, initial-scale=1.0"/>
          <meta http-equiv="X-UA-Compatible" content="IE=edge"/>
          <meta name="color-scheme" content="dark"/>
          <meta name="supported-color-schemes" content="dark"/>
          <title>Knowball - Den&uacute;ncia recebida</title>
          <style>
            :root { color-scheme: dark; supported-color-schemes: dark; }
            u + .body .gmail-blend-screen { background: #000; mix-blend-mode: screen; }
            u + .body .gmail-blend-difference { background: #000; mix-blend-mode: difference; }
          </style>
        </head>
        <body class="body" bgcolor="#06060f"
              style="margin:0;padding:0;background-color:#06060f;background-image:linear-gradient(#06060f,#06060f);font-family:'Segoe UI',Helvetica,Arial,sans-serif;">

          <div style="display:none;max-height:0;overflow:hidden;opacity:0;color:#06060f;font-size:1px;line-height:1px;">
            Sua den&uacute;ncia foi recebida. Protocolo %1$s.
          </div>

          <div class="gmail-blend-screen">
            <div class="gmail-blend-difference">

              <table role="presentation" width="100%%" cellpadding="0" cellspacing="0" border="0"
                     bgcolor="#06060f"
                     style="background-color:#06060f;background-image:linear-gradient(#06060f,#06060f);">
                <tr>
                  <td align="center" style="padding:40px 16px;">

                    <table role="presentation" width="600" cellpadding="0" cellspacing="0" border="0"
                           bgcolor="#0d0d18"
                           style="width:100%%;max-width:600px;background-color:#0d0d18;background-image:linear-gradient(#0d0d18,#0d0d18);border:1px solid #23233a;border-radius:20px;overflow:hidden;">

                      <!-- Cabecalho -->
                      <tr>
                        <td align="center" bgcolor="#0b0b16"
                            style="padding:38px 40px 30px;background-color:#0b0b16;background-image:linear-gradient(#0b0b16,#0b0b16);border-bottom:1px solid #1c1c2d;">
                          <p style="margin:0;color:#ffffff;font-size:30px;font-weight:700;letter-spacing:-0.8px;line-height:1.1;">
                            Knowball
                          </p>
                          <p style="margin:12px 0 0;color:#c4c4d6;font-size:11px;font-weight:600;letter-spacing:2.4px;text-transform:uppercase;">
                            Sistema de Den&uacute;ncias
                          </p>
                          <p style="margin:6px 0 0;color:#9494ab;font-size:12px;line-height:1.5;">
                            Categorias de base do futebol brasileiro masculino
                          </p>
                        </td>
                      </tr>

                      <!-- Protocolo -->
                      <tr>
                        <td align="center" style="padding:32px 40px 0;">
                          <table role="presentation" cellpadding="0" cellspacing="0" border="0" align="center">
                            <tr>
                              <td align="center" bgcolor="#12121f"
                                  style="padding:18px 38px;background-color:#12121f;background-image:linear-gradient(#12121f,#12121f);border:1px solid #2a2a42;border-radius:14px;">
                                <p style="margin:0;color:#a9a9c0;font-size:11px;font-weight:600;letter-spacing:2.4px;text-transform:uppercase;">
                                  Protocolo gerado
                                </p>
                                <p style="margin:10px 0 0;color:#ffffff;font-size:28px;font-weight:700;letter-spacing:3px;font-family:'SFMono-Regular',Consolas,'Courier New',monospace;">
                                  %1$s
                                </p>
                              </td>
                            </tr>
                          </table>
                        </td>
                      </tr>

                      <!-- Mensagem -->
                      <tr>
                        <td style="padding:28px 40px 0;">
                          <p style="margin:0;color:#e4e4ee;font-size:15px;line-height:1.6;">
                            Ol&aacute;, <strong style="color:#ffffff;">%2$s</strong>!
                          </p>
                          <p style="margin:12px 0 0;color:#cfcfdc;font-size:15px;line-height:1.6;">
                            Sua den&uacute;ncia foi
                            <strong style="color:#9fb1ff;">recebida com sucesso</strong>
                            em <strong style="color:#ffffff;">%3$s</strong>
                            e j&aacute; est&aacute; na fila de an&aacute;lise da nossa equipe.
                          </p>
                        </td>
                      </tr>

                      <!-- Relato -->
                      <tr>
                        <td style="padding:24px 40px 0;">
                          <p style="margin:0 0 10px;color:#9a9ab2;font-size:11px;font-weight:600;letter-spacing:1.8px;text-transform:uppercase;">
                            Seu relato
                          </p>
                          <table role="presentation" width="100%%" cellpadding="0" cellspacing="0" border="0">
                            <tr>
                              <td bgcolor="#0a0a14"
                                  style="padding:16px 20px;background-color:#0a0a14;background-image:linear-gradient(#0a0a14,#0a0a14);border:1px solid #1e1e30;border-left:3px solid #9fb1ff;border-radius:10px;">
                                <p style="margin:0;color:#d2d2de;font-size:14px;line-height:1.7;white-space:pre-wrap;">%4$s</p>
                              </td>
                            </tr>
                          </table>
                        </td>
                      </tr>

                      <!-- Confidencialidade -->
                      <tr>
                        <td style="padding:24px 40px 0;">
                          <table role="presentation" width="100%%" cellpadding="0" cellspacing="0" border="0">
                            <tr>
                              <td bgcolor="#10101c"
                                  style="padding:16px 20px;background-color:#10101c;background-image:linear-gradient(#10101c,#10101c);border:1px solid #22223a;border-radius:12px;">
                                <p style="margin:0;color:#a2a2b8;font-size:13px;line-height:1.6;">
                                  <strong style="color:#e4e4ee;">Confidencialidade garantida.</strong>
                                  Suas informa&ccedil;&otilde;es s&atilde;o protegidas e utilizadas exclusivamente
                                  para a an&aacute;lise desta den&uacute;ncia.
                                </p>
                              </td>
                            </tr>
                          </table>
                        </td>
                      </tr>

                      <!-- Rodape -->
                      <tr>
                        <td align="center" style="padding:32px 40px;">
                          <p style="margin:0;color:#7c7c94;font-size:12px;line-height:1.7;">
                            Este e-mail foi enviado automaticamente pelo sistema Knowball.<br/>
                            Knowball &middot; Integridade no Futebol de Base
                          </p>
                        </td>
                      </tr>

                    </table>
                  </td>
                </tr>
              </table>

            </div>
          </div>
        </body>
        </html>
        """.formatted(
                esc(report.getProtocol()),
                esc(userName),
                esc(dataFormatada),
                esc(report.getContent())
        );
    }
}