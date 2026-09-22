package br.com.fiap.knowball.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record SendVerificationCodeRequest(
        @NotBlank @Email
        String email
) {}