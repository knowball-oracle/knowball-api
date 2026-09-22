package br.com.fiap.knowball.service;

import br.com.fiap.knowball.model.EmailVerificationToken;
import br.com.fiap.knowball.repository.EmailVerificationTokenRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;
import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class EmailVerificationService {

    private static final int CODE_LENGTH = 6;
    private static final long CODE_EXPIRATION_MINUTES = 10;

    private static final long VERIFICATION_VALIDITY_MINUTES = 15;

    private final EmailVerificationTokenRepository tokenRepository;
    private final EmailService emailService;
    private final SecureRandom random = new SecureRandom();

    public void generateAndSendCode(String email) {
        String code = generateSixDigitCode();

        EmailVerificationToken token = EmailVerificationToken.builder()
                .email(email)
                .code(code)
                .expiresAt(LocalDateTime.now().plusMinutes(CODE_EXPIRATION_MINUTES))
                .used(false)
                .build();

        tokenRepository.save(token);

        String subject = "Seu código de verificação Knowball";
        String htmlBody = buildEmailBody(code);

        emailService.sendVerificationCode(email, subject, htmlBody);
    }

    public void verifyCode(String email, String code) {
        EmailVerificationToken token = tokenRepository
                .findTopByEmailAndCodeAndUsedFalseOrderByIdDesc(email, code)
                .orElseThrow(() -> new IllegalArgumentException("Código inválido."));

        if (token.getExpiresAt().isBefore(LocalDateTime.now())) {
            throw new IllegalArgumentException("Código expirado. Solicite um novo.");
        }

        token.setUsed(true);
        token.setVerifiedAt(LocalDateTime.now());
        tokenRepository.save(token);
    }

    public boolean hasRecentVerifiedEmail(String email) {
        return tokenRepository.findTopByEmailAndUsedTrueOrderByVerifiedAtDesc(email)
                .map(token -> token.getVerifiedAt() != null
                        && token.getVerifiedAt().isAfter(
                        LocalDateTime.now().minusMinutes(VERIFICATION_VALIDITY_MINUTES)))
                .orElse(false);
    }

    private String generateSixDigitCode() {
        int number = random.nextInt(1_000_000);
        return String.format("%0" + CODE_LENGTH + "d", number);
    }

    private String buildEmailBody(String code) {
        return """
                <div style="font-family: Arial, sans-serif; max-width: 480px; margin: 0 auto;">
                    <h2 style="color: #0D1B14;">Knowball — Verificação de e-mail</h2>
                    <p>Use o código abaixo para confirmar seu e-mail e concluir o cadastro:</p>
                    <div style="font-size: 32px; font-weight: bold; letter-spacing: 8px;
                                background: #f5f5f5; padding: 16px; text-align: center;
                                border-radius: 8px; margin: 16px 0;">
                        %s
                    </div>
                    <p style="color: #666; font-size: 13px;">
                        Este código expira em %d minutos. Se você não solicitou este cadastro,
                        pode ignorar este e-mail.
                    </p>
                </div>
                """.formatted(code, CODE_EXPIRATION_MINUTES);
    }
}