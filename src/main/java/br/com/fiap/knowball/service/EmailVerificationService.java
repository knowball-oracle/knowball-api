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
            <div style="font-family:sans-serif;max-width:480px;margin:auto;padding:32px">
              <h2 style="color:#01696f">KnowBall — Verificação de e-mail</h2>
              <p>Use o código abaixo para confirmar seu cadastro:</p>
              <div style="font-size:36px;font-weight:bold;letter-spacing:12px;
                          color:#0f3638;background:#cedcd8;padding:16px 24px;
                          border-radius:8px;text-align:center;margin:24px 0">
                %s
              </div>
              <p style="color:#7a7974">
                Válido por %d minutos. Ignore este e-mail se não realizou o cadastro.
              </p>
            </div>
            """.formatted(code, expirationMinutes);
    }
}