package br.com.fiap.knowball.dto;

public record ReportResponseDTO(
        Long id,
        String protocol,
        String email
) {}