package br.com.fiap.knowball.dto;

import br.com.fiap.knowball.model.AnalysisResultType;

public record ReportByResultDTO(AnalysisResultType result, String label, Long count) {

    public ReportByResultDTO(AnalysisResultType result, Long count) {
        this(result, labelOf(result), count);
    }

    private static String labelOf(AnalysisResultType result) {
        return switch (result) {
            case POSITIVE -> "Positivo";
            case NEUTRAL -> "Neutro";
            case NEGATIVE -> "Negativo";
            default -> result.name();
        };
    }
}
