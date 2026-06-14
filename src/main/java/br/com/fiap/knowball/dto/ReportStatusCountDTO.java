package br.com.fiap.knowball.dto;

import br.com.fiap.knowball.model.ReportStatusType;

public record ReportStatusCountDTO(
        ReportStatusType status,
        Long count
) {}
