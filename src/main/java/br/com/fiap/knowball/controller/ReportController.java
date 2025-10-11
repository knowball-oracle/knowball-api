package br.com.fiap.knowball.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.fiap.knowball.model.Report;
import br.com.fiap.knowball.model.ReportStatusType;
import br.com.fiap.knowball.service.ReportService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Tag(name = "Report", description = "Endpoints de gerenciamento de denúncias")
@RestController
@RequestMapping("reports")
@RequiredArgsConstructor
@Slf4j
public class ReportController {
    
    private final ReportService reportService;

    @Operation(summary = "Listar todas as denúncias", description = "Retorna a lista de todas as denúncias cadastradas.")
    @ApiResponse(responseCode = "200", description = "Lista de denúncias retornada com sucesso")
    @GetMapping
    public List<Report> getAll() {
        log.info("buscando todas as denúncias");
        return reportService.findAll();
    }

    @Operation(summary = "Buscar denúncia por ID", description = "Retorna uma denúncia pelo seu identificador único.")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Denúncia encontrada"),
        @ApiResponse(responseCode = "404", description = "Denúncia não encontrada")
    })
    @GetMapping("{id}")
    public Report getById(@PathVariable Long id) {
        log.info("buscando denúncia pelo id: {}", id);
        return reportService.findById(id);
    }

    @Operation(summary = "Buscar denúncias por status", description = "Retorna todas as denúncias com o status informado.")
    @ApiResponse(responseCode = "200", description = "Lista de denúncias filtrada por status retornada com sucesso")
    @GetMapping("/status/{status}")
    public List<Report> getReportsByStatus(@PathVariable ReportStatusType status) {
        log.info("buscando denúncias com status: {}", status);
        return reportService.findByStatus(status);
    }

    @Operation(summary = "Criar nova denúncia", description = "Cria uma nova denúncia com os dados fornecidos.")
    @ApiResponse(responseCode = "201", description = "Denúncia criada com sucesso")
    @PostMapping
    public ResponseEntity<Report> create(@Valid @RequestBody Report report) {
        log.info("criando nova denúncia com protocolo {}", report.getProtocol());
        Report created = reportService.save(report);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }
}
