package br.com.fiap.knowball.service;

import br.com.fiap.knowball.model.EmailVerificationToken;
import br.com.fiap.knowball.repository.EmailVerificationTokenRepository;
import br.com.fiap.knowball.repository.UserRepository;
import com.resend.Resend;
import com.resend.core.exception.ResendException;
import com.resend.services.emails.model.CreateEmailOptions;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Random;

@Service
@Slf4j
public class EmailVerificationService {

    private final EmailVerificationTokenRepository tokenRepo;
    private final UserRepository userRepo;
    private final Resend resend;

    @Value("${app.email.from}")
    private String fromEmail;

    @Value("${app.verification.expiration-minutes}")
    private int expirationMinutes;

    public EmailVerificationService(
            EmailVerificationTokenRepository tokenRepo,
            UserRepository userRepo,
            @Value("${resend.api-key}") String apiKey) {
        this.tokenRepo = tokenRepo;
        this.userRepo = userRepo;
        this.resend = new Resend(apiKey);
    }

    @Transactional
    public void sendVerificationCode(String email) {
        tokenRepo.deleteByEmail(email);

        String code = String.format("%06d", new Random().nextInt(1_000_000));

        tokenRepo.save(EmailVerificationToken.builder()
                .email(email)
                .code(code)
                .expiresAt(LocalDateTime.now().plusMinutes(expirationMinutes))
                .used(false)
                .build());

        CreateEmailOptions emailReq = CreateEmailOptions.builder()
                .from(fromEmail)
                .to(email)
                .subject("Seu código de verificação - KnowBall")
                .html(buildEmailHtml(code))
                .build();

        try {
            resend.emails().send(emailReq);
            log.info("Código de verificação enviado para: {}", email);
        } catch (ResendException e) {
            log.error("Erro ao enviar e-mail de verificação para {}: {}", email, e.getMessage());
            throw new RuntimeException("Falha ao enviar e-mail de verificação.", e);
        }
    }

    @Transactional
    public boolean verifyCode(String email, String code) {
        return tokenRepo.findByEmailAndCodeAndUsedFalse(email, code)
                .filter(t -> t.getExpiresAt().isAfter(LocalDateTime.now()))
                .map(t -> {
                    t.setUsed(true);
                    tokenRepo.save(t);
                    userRepo.findByEmail(email).ifPresent(u -> {
                        u.setEmailVerified(true);
                        userRepo.save(u);
                    });
                    return true;
                })
                .orElse(false);
    }

    private String buildEmailHtml(String code) {
        return """
        <!DOCTYPE html>
        <html lang="pt-BR">
        <head><meta charset="UTF-8"><meta name="viewport" content="width=device-width,initial-scale=1.0"></head>
        <body style="margin:0;padding:0;background-color:#06060f;font-family:'Helvetica Neue',Arial,sans-serif;">
          <table width="100%%" cellpadding="0" cellspacing="0" style="background-color:#06060f;padding:40px 16px;">
            <tr>
              <td align="center">
                <table width="100%%" cellpadding="0" cellspacing="0"
                  style="max-width:480px;
                         background:linear-gradient(135deg,rgba(255,255,255,0.06) 0%%,rgba(255,255,255,0.02) 100%%);
                         border:1px solid rgba(255,255,255,0.08);
                         border-radius:24px;
                         padding:40px 36px;">

                  <!-- Logo / título -->
                  <tr>
                    <td align="center" style="padding-bottom:28px;">
                      <span style="font-size:22px;font-weight:700;color:#ffffff;letter-spacing:-0.5px;">
                        Knowball</span>
                      </span>
                    </td>
                  </tr>

                  <!-- Heading -->
                  <tr>
                    <td style="padding-bottom:8px;">
                      <p style="margin:0;font-size:20px;font-weight:600;color:#ffffff;">
                        Verifique seu e-mail
                      </p>
                    </td>
                  </tr>

                  <!-- Subtítulo -->
                  <tr>
                    <td style="padding-bottom:28px;">
                      <p style="margin:0;font-size:14px;color:rgba(255,255,255,0.4);line-height:1.6;">
                        Use o código abaixo para confirmar seu cadastro na plataforma.
                      </p>
                    </td>
                  </tr>

                  <!-- Código -->
                  <tr>
                    <td align="center" style="padding-bottom:28px;">
                      <div style="display:inline-block;
                                  background:rgba(255,255,255,0.05);
                                  border:1px solid rgba(255,255,255,0.1);
                                  border-radius:16px;
                                  padding:20px 36px;">
                        <span style="font-size:40px;
                                     font-weight:700;
                                     letter-spacing:14px;
                                     color:#ffffff;
                                     font-variant-numeric:tabular-nums;">
                          %s
                        </span>
                      </div>
                    </td>
                  </tr>

                  <!-- Validade -->
                  <tr>
                    <td align="center" style="padding-bottom:28px;">
                      <p style="margin:0;font-size:13px;color:rgba(255,255,255,0.3);">
                        Válido por <strong style="color:rgba(255,255,255,0.5);">%d minutos</strong>.
                      </p>
                    </td>
                  </tr>

                  <!-- Divider -->
                  <tr>
                    <td style="padding-bottom:20px;">
                      <div style="height:1px;background:rgba(255,255,255,0.07);"></div>
                    </td>
                  </tr>

                  <!-- Aviso de segurança -->
                  <tr>
                    <td>
                      <p style="margin:0;font-size:12px;color:rgba(255,255,255,0.2);text-align:center;line-height:1.6;">
                        Se você não realizou este cadastro, ignore este e-mail.<br>
                        Nenhuma ação é necessária.
                      </p>
                    </td>
                  </tr>

                </table>
              </td>
            </tr>
          </table>
        </body>
        </html>
        """.formatted(code, expirationMinutes);
    }
}