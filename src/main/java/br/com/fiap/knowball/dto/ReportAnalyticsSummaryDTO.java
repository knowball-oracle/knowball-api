package br.com.fiap.knowball.dto;

import java.util.List;

public record ReportAnalyticsSummaryDTO(
        long totalReports,
        List<ReportStatusCountDTO> byStatus,
        List<ReportByChampionshipDTO> byChampionship,
        long reportsThisMonth,
        long resolvedCount,
        long pendingCount
) {}
