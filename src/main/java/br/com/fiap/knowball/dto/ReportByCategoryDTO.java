package br.com.fiap.knowball.dto;

public record ReportByCategoryDTO(String category, String label, Long count) {

    public ReportByCategoryDTO(String category, Long count) {
        this(category, category.replace("SUB_", "Sub-"), count);
    }
}