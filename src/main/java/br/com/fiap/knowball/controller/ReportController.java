package br.com.fiap.knowball.controller;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.fiap.knowball.assembler.ReportModelAssembler;
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
    private final ReportModelAssembler assembler;

    @SuppressWarnings("null")
    @Operation(summary = "Listar todas as denúncias", description = "Retorna a lista de todas as denúncias cadastradas.")
    @ApiResponse(responseCode = "200", description = "Lista de denúncias retornada com sucesso")
    @GetMapping
    public CollectionModel<EntityModel<Report>> getAll() {
        log.info("buscando todas as denúncias");
        List<EntityModel<Report>> reports = reportService.findAll().stream()
            .map(assembler::toModel)
            .collect(Collectors.toList());

        return CollectionModel.of(reports,
            linkTo(methodOn(ReportController.class).getAll()).withSelfRel()
        );
    }

    @SuppressWarnings("null")
    @Operation(summary = "Buscar denúncia por ID", description = "Retorna uma denúncia pelo seu identificador único.")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Denúncia encontrada"),
        @ApiResponse(responseCode = "404", description = "Denúncia não encontrada")
    })
    @GetMapping("{id}")
    public EntityModel<Report> getById(@PathVariable Long id) {
        log.info("buscando denúncia pelo id: {}", id);
        Report report = reportService.findById(id);
        return assembler.toModel(report);
    }

    @SuppressWarnings("null")
    @Operation(summary = "Buscar denúncias por status", description = "Retorna todas as denúncias com o status informado.")
    @ApiResponse(responseCode = "200", description = "Lista de denúncias filtrada por status retornada com sucesso")
    @GetMapping("/status/{status}")
    public CollectionModel<EntityModel<Report>> getReportsByStatus(@PathVariable ReportStatusType status) {
        log.info("buscando denúncias com status: {}", status);
        List<EntityModel<Report>> reports = reportService.findByStatus(status).stream()
            .map(assembler::toModel)
            .collect(Collectors.toList());

        return CollectionModel.of(reports,
            linkTo(methodOn(ReportController.class).getReportsByStatus(status))
                .withSelfRel()
                .withTitle("List reports by status"),
            linkTo(methodOn(ReportController.class).getAll())
                .withRel("all-reports")
                .withTitle("List all reports")
        );
    }

    @SuppressWarnings("null")
    @Operation(summary = "Criar nova denúncia", description = "Cria uma nova denúncia com os dados fornecidos.")
    @ApiResponse(responseCode = "201", description = "Denúncia criada com sucesso")
    @PostMapping
    public ResponseEntity<EntityModel<Report>> create(@Valid @RequestBody Report report) {
        log.info("criando nova denúncia com protocolo {}", report.getProtocol());
        Report created = reportService.save(report);
        EntityModel<Report> entityModel = assembler.toModel(created);

        return ResponseEntity
            .created(linkTo(methodOn(ReportController.class).getById(created.getId())).toUri())
            .body(entityModel);
    }
}
