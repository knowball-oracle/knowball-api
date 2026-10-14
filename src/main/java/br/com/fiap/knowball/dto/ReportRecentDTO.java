package br.com.fiap.knowball.dto;

import br.com.fiap.knowball.model.AnalysisResultType;
import br.com.fiap.knowball.model.ReportStatusType;

import java.time.Instant;

public record ReportRecentDTO(
        Long id,
        String protocol,
        ReportStatusType status,
        AnalysisResultType analysisResult,
        Instant date,
        String refereeName,
        String championshipName
) {}