package br.com.fiap.knowball.dto;

public record ReportByChampionshipDTO(
   String championshipName,
   String category,
   Long count
) {}
