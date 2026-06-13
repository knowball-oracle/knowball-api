package br.com.fiap.knowball.repository;

import br.com.fiap.knowball.model.EmailVerificationToken;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface EmailVerificationTokenRepository extends JpaRepository<EmailVerificationToken, Long> {

    Optional<EmailVerificationToken> findByEmailAndCodeAndUsedFalse(String email, String code);

    void deleteByEmail(String email);
}
