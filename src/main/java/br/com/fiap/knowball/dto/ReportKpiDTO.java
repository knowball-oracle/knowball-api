package br.com.fiap.knowball.dto;

public record ReportKpiDTO(
        long totalReports,
        long reportsThisMonth,
        long resolvedCount,
        long pendingCount
) {}
