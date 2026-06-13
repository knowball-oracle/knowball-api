package br.com.fiap.knowball.dto;

public record VerifyEmailRequest (
        String email,
        String code
){}
