package br.com.fiap.knowball.service;

import java.time.Duration;
import java.time.Instant;
import java.util.List;

import br.com.fiap.knowball.model.User;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;
import org.springframework.stereotype.Service;

@Service
public class TokenService {

    private final JwtEncoder jwtEncoder;
    private final Duration tokenTtl;

    public TokenService(
            JwtEncoder jwtEncoder,
            @Value("${app.jwt.ttl-hours:1}") long ttlHours) {

        if (ttlHours <= 0) {
            throw new IllegalArgumentException(
                    "A validade do JWT deve ser maior que zero."
            );
        }

        this.jwtEncoder = jwtEncoder;
        this.tokenTtl = Duration.ofHours(ttlHours);
    }

    public String generateToken(User user) {
        Instant now = Instant.now();

        JwtClaimsSet claims = JwtClaimsSet.builder()
                .issuer("knowball-api")
                .issuedAt(now)
                .expiresAt(now.plus(tokenTtl))
                .subject(user.getEmail())
                .claim("email", user.getEmail())
                .claim("name", user.getName())
                .claim("role", List.of(user.getRole().name()))
                .build();

        return jwtEncoder
                .encode(JwtEncoderParameters.from(claims))
                .getTokenValue();
    }
}