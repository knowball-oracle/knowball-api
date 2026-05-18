package br.com.fiap.knowball.controller;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

import java.util.List;
import java.util.stream.Collectors;

import br.com.fiap.knowball.model.AnalysisResultType;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

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
@SecurityRequirement(name = "bearerAuth")
@RestController
@RequestMapping("/reports")
@RequiredArgsConstructor
@Slf4j
public class ReportController {
    
    private final ReportService reportService;
    private final ReportModelAssembler assembler;

    public record UpdateStatusRequest(
            ReportStatusType status,
            AnalysisResultType analysisResultType
    ) {}

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

    @Operation(summary = "Atualizar status da denúncia", description = "Atualiza o status e o resultado da análise de uma denúncia.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Status atualizado com sucesso"),
            @ApiResponse(responseCode = "404", description = "Denúncia não encontrada")
    })
    @PutMapping("/{id}/status")
    public EntityModel<Report> updateStatus(
            @PathVariable Long id,
            @RequestBody UpdateStatusRequest request) {
        log.info("atualizando status da denúncia {}: {} / {}", id, request.status(), request.analysisResultType());
        Report updated = reportService.updateStatus(id, request.status(), request.analysisResultType());
        return assembler.toModel(updated);
    }

    @Operation(summary = "Excluir denúncia", description = "Permite excluir uma denúncia. O usuário comum só pode excluir a própria denúncia; o administrador pode excluir qualquer uma.")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Denúncia excluída com sucesso"),
            @ApiResponse(responseCode = "403", description = "Acesso negado"),
            @ApiResponse(responseCode = "404", description = "Denúncia não encontrada")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id, Authentication authentication) {
        log.info("excluindo denúncia {}", id);
        reportService.deleteReport(id, authentication.getName());
        return ResponseEntity.noContent().build();
    }
}
