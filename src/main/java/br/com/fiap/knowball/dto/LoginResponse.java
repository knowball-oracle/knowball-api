package br.com.fiap.knowball.dto;

public record LoginResponse(
    String token,
    String email,
    String name,
    String role
) {}
