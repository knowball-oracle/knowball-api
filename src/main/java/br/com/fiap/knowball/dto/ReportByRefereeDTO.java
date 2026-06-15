package br.com.fiap.knowball.dto;

public record ReportByRefereeDTO(
        Long refereeId,
        String refereeName,
        Long reportsCount
) {}
