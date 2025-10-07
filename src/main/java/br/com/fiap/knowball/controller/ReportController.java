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
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("reports")
@RequiredArgsConstructor
@Slf4j
public class ReportController {
    
    private final ReportService reportService;

    @GetMapping
    public List<Report> getAll() {
        log.info("buscando todas as denúncias");
        return reportService.findAll();
    }

    @GetMapping("{id}")
    public Report getById(@PathVariable Long id) {
        log.info("buscando denúncia pelo id: {}", id);
        return reportService.findById(id);
    }

    @GetMapping("/status/{status}")
    public List<Report> getReportsByStatus(@PathVariable ReportStatusType status) {
        log.info("buscando denúncias com status: {}", status);
        return reportService.findByStatus(status);
    }

    @PostMapping
    public ResponseEntity<Report> create(@Valid @RequestBody Report report) {
        log.info("criando nova denúncia com protocolo {}", report.getProtocol());
        Report created = reportService.save(report);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }
}
