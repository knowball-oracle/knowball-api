package br.com.fiap.knowball.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record ChatRequest(
        @NotBlank(message = "A mensagem não pode ser vazia")
        @Size(max = 1000, message = "Mensagem muito longa")
        String message
) {}
