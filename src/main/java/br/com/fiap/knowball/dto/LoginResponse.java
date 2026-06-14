package br.com.fiap.knowball.dto;

public record LoginResponse(
    String token,
    Long id,
    String email,
    String name,
    String role,
    String profilePicture
) {}
