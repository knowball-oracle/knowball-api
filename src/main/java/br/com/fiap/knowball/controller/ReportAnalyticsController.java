package br.com.fiap.knowball.controller;

import br.com.fiap.knowball.dto.ReportAnalyticsSummaryDTO;
import br.com.fiap.knowball.dto.ReportByChampionshipDTO;
import br.com.fiap.knowball.dto.ReportKpiDTO;
import br.com.fiap.knowball.dto.ReportStatusCountDTO;
import br.com.fiap.knowball.service.ReportAnalyticsService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Tag(name = "Analytics", description = "Endpoints de analytics para integração com Oracle APEX")
@SecurityRequirement(name = "bearerAuth")
@RestController
@RequestMapping("/analytics/reports")
@RequiredArgsConstructor
@Slf4j
public class ReportAnalyticsController {

    private final ReportAnalyticsService analyticsService;

    @Operation(summary = "Contagem por status", description = "Retorna a quantidade de denúncias agrupada por status.")
    @ApiResponse(responseCode = "200", description = "Contagem retornada com sucesso")
    @GetMapping("/by-status")
    public ResponseEntity<List<ReportStatusCountDTO>> countByStatus() {
        log.info("analytics: contagem de denúncias por status");
        return ResponseEntity.ok(analyticsService.getCountByStatus());
    }

    @Operation(summary = "Contagem por campeonato", description = "Retorna a quantidade de denúncias agrupada por campeonato e categoria.")
    @ApiResponse(responseCode = "200", description = "Contagem retornada com sucesso")
    @GetMapping("/by-championship")
    public ResponseEntity<List<ReportByChampionshipDTO>> countByChampionship() {
        log.info("analytics: contagem de denúncias por campeonato");
        return ResponseEntity.ok(analyticsService.getCountByChampionship());
    }

    @Operation(summary = "Resumo geral (dashboard)", description = "Retorna um resumo consolidado com totais, por status, por campeonato e do mês atual.")
    @ApiResponse(responseCode = "200", description = "Resumo retornado com sucesso")
    @GetMapping("/summary")
    public ResponseEntity<ReportAnalyticsSummaryDTO> getSummary() {
        log.info("analytics: resumo geral das denúncias");
        return ResponseEntity.ok(analyticsService.getSummary());
    }

    @Operation(summary = "KPIs do dashboard",
            description = "Retorna apenas os indicadores principais (totais) para o dashboard.")
    @ApiResponse(responseCode = "200", description = "KPIs retornados com sucesso")
    @GetMapping("/kpis")
    public ResponseEntity<ReportKpiDTO> getKpis() {
        log.info("analytics: KPIs do dashboard");
        return ResponseEntity.ok(analyticsService.getKpis());
    }
}