package br.com.fiap.knowball.service;

import br.com.fiap.knowball.model.Report;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import sendinblue.ApiClient;
import sendinblue.Configuration;
import sendinblue.auth.ApiKeyAuth;
import sibApi.TransactionalEmailsApi;
import sibModel.CreateSmtpEmail;
import sibModel.SendSmtpEmail;
import sibModel.SendSmtpEmailSender;
import sibModel.SendSmtpEmailTo;

import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Service
@Slf4j
public class EmailService {

    @Value("${brevo.api.key}")
    private String apiKey;

    @Value("${brevo.sender.email}")
    private String senderEmail;

    @Value("${brevo.sender.name}")
    private String senderName;

    private static final DateTimeFormatter FORMATTER =
            DateTimeFormatter.ofPattern("dd/MM/yyyy 'às' HH:mm")
                    .withZone(ZoneId.of("America/Sao_Paulo"));

    @Async
    public void sendReportConfirmation(Report report) {
        try {
            ApiClient client = Configuration.getDefaultApiClient();
            ApiKeyAuth apiKeyAuth = (ApiKeyAuth) client.getAuthentication("api-key");
            apiKeyAuth.setApiKey(apiKey);

            TransactionalEmailsApi api = new TransactionalEmailsApi(client);

            SendSmtpEmail email = new SendSmtpEmail();

            SendSmtpEmailSender sender = new SendSmtpEmailSender();
            sender.setEmail(senderEmail);
            sender.setName(senderName);
            email.setSender(sender);

            SendSmtpEmailTo recipient = new SendSmtpEmailTo();
            recipient.setEmail(report.getUser().getEmail());
            email.setTo(List.of(recipient));

            email.setSubject("✅ Denúncia recebida – Protocolo " + report.getProtocol());
            email.setHtmlContent(buildHtml(report));

            CreateSmtpEmail result = api.sendTransacEmail(email);
            log.info("E-mail enviado via Brevo para {} – protocolo {} – messageId: {}",
                    report.getUser().getEmail(), report.getProtocol(), result.getMessageId());

        } catch (Exception e) {
            log.error("Falha ao enviar e-mail via Brevo para protocolo {}: {}",
                    report.getProtocol(), e.getMessage());
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
                            ⚽ Knowball
                          </h1>
                          <p style="margin:6px 0 0;color:#a8d5d7;font-size:13px;letter-spacing:1px;text-transform:uppercase;">
                            Sistema de Denúncias – Categorias de Base do Futebol Brasileiro Masculino
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
                            Olá, <strong style="color:#ffffff;">%s</strong>!
                          </p>
                          <p style="margin:12px 0 0;color:#cdccca;font-size:15px;line-height:1.6;">
                            Sua denúncia foi <strong style="color:#4f98a3;">recebida com sucesso</strong>
                            em <strong>%s</strong> e já está na fila de análise da nossa equipe.
                            Em breve você receberá uma atualização sobre o andamento.
                          </p>
                        </td>
                      </tr>
                      <tr>
                        <td style="padding:24px 40px 0;">
                          <p style="margin:0 0 10px;color:#797876;font-size:11px;text-transform:uppercase;
                                    letter-spacing:1.5px;font-weight:600;">Seu relato</p>
                          <div style="background:#141312;border-left:3px solid #01696f;border-radius:0 8px 8px 0;padding:16px 20px;">
                            <p style="margin:0;color:#cdccca;font-size:14px;line-height:1.7;white-space:pre-wrap;">%s</p>
                          </div>
                        </td>
                      </tr>
                      <tr>
                        <td style="padding:24px 40px 0;">
                          <div style="background:#1a1918;border:1px solid #2a2927;border-radius:10px;padding:16px 20px;">
                            <p style="margin:0;color:#797876;font-size:13px;line-height:1.6;">
                              🔒 <strong style="color:#cdccca;">Confidencialidade garantida.</strong>
                              Suas informações são protegidas e utilizadas exclusivamente para a análise desta denúncia.
                            </p>
                          </div>
                        </td>
                      </tr>
                      <tr>
                        <td style="padding:32px 40px;text-align:center;border-top:1px solid #2a2927;">
                          <p style="margin:0;color:#5a5957;font-size:12px;line-height:1.6;">
                            Este e-mail foi enviado automaticamente pelo sistema Knowball.<br/>
                            © %d Knowball – Integridade no Futebol de Base
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
                report.getContent(),
                java.time.LocalDate.now().getYear()
        );
    }
}