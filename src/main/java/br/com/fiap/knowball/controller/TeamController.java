package br.com.fiap.knowball.controller;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.fiap.knowball.assembler.TeamModelAssembler;
import br.com.fiap.knowball.model.Team;
import br.com.fiap.knowball.service.TeamService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Tag(name = "Team", description = "Endpoints de gerenciamento de times")
@RestController
@RequestMapping("teams")
@RequiredArgsConstructor
@Slf4j
public class TeamController {
    
    private final TeamService teamService;
    private final TeamModelAssembler assembler;

    @SuppressWarnings("null")
    @Operation(summary = "Listar todos os times", description = "Retorna a lista de todos os times cadastrados.")
    @ApiResponse(responseCode = "200", description = "Lista de times retornada com sucesso")
    @GetMapping
    public CollectionModel<EntityModel<Team>> getAll() {
        log.info("buscando todos os times");
        List<EntityModel<Team>> teams = teamService.findAll().stream()
            .map(assembler::toModel)
            .collect(Collectors.toList());

        return CollectionModel.of(teams,
            linkTo(methodOn(TeamController.class).getAll()).withSelfRel()
        );
    }

    @SuppressWarnings("null")
    @Operation(summary = "Buscar time por ID", description = "Retorna um time pelo seu identificador único.")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Time encontrado"),
        @ApiResponse(responseCode = "404", description = "Time não encontrado")
    })
    @GetMapping("{id}")
    public EntityModel<Team> getById(@PathVariable Long id) {
        log.info("buscando time por id: {}", id);
        Team team = teamService.findById(id);
        return assembler.toModel(team);
    }

    @SuppressWarnings("null")
    @Operation(summary = "Criar novo time", description = "Cria um novo time com os dados fornecidos.")
    @ApiResponse(responseCode = "201", description = "Time criado com sucesso")
    @PostMapping
    public ResponseEntity<EntityModel<Team>> create(@Valid @RequestBody Team team) {
        log.info("criando um novo time: {}", team);
        Team created = teamService.save(team);
        EntityModel<Team> entityModel = assembler.toModel(created);

        return ResponseEntity
            .created(linkTo(methodOn(TeamController.class).getById(created.getId())).toUri())
            .body(entityModel);
    }

    @SuppressWarnings("null")
    @Operation(summary = "Atualizar time", description = "Atualiza os dados de um time existente.")
    @ApiResponse(responseCode = "200", description = "Time atualizado com sucesso")
    @PutMapping("{id}")
    public EntityModel<Team> update(@PathVariable Long id, @Valid @RequestBody Team team) {
        log.info("atualizando time com id: {}", id);
        Team updated = teamService.update(id, team);
        return assembler.toModel(updated);
    }

    @SuppressWarnings("null")
    @Operation(summary = "Deletar time", description = "Remove um time pelo seu ID.")
    @ApiResponses({
        @ApiResponse(responseCode = "204", description = "Time deletado com sucesso"),
        @ApiResponse(responseCode = "404", description = "Time não encontrado")
    })
    @DeleteMapping("{id}")
    public ResponseEntity<Void> destroy(@PathVariable Long id) {
        log.info("deletando time com id: {}", id);
        teamService.destroy(id);
        return ResponseEntity.noContent().build();
    }
}
