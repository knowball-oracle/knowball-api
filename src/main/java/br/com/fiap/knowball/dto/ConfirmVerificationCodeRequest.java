package br.com.fiap.knowball.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

public record ConfirmVerificationCodeRequest(
        @NotBlank @Email
        String email,

        @NotBlank @Pattern(regexp = "^\\d{6}$", message = "Código deve ter 6 dígitos.")
        String code
) {}