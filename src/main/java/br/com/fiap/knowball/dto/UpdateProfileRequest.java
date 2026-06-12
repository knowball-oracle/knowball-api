package br.com.fiap.knowball.dto;

import jakarta.validation.constraints.Size;

public record UpdateProfileRequest(

        @Size(max = 100, message = "O nome deve ter no máximo 100 caracteres")
        String name,
        String profilePicture
) {}
