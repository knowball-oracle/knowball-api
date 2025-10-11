package br.com.fiap.knowball.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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

    @Operation(summary = "Listar todos os times", description = "Retorna a lista de todos os times cadastrados.")
    @ApiResponse(responseCode = "200", description = "Lista de times retornada com sucesso")
    @GetMapping
    public List<Team> getAll() {
        log.info("buscando todos os times");
        return teamService.findAll();
    }

    @Operation(summary = "Buscar time por ID", description = "Retorna um time pelo seu identificador único.")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Time encontrado"),
        @ApiResponse(responseCode = "404", description = "Time não encontrado")
    })
    @GetMapping("{id}")
    public Team getById(@PathVariable Long id) {
        log.info("buscando time por id: {}", id);
        return teamService.findById(id);
    }

    @Operation(summary = "Criar novo time", description = "Cria um novo time com os dados fornecidos.")
    @ApiResponse(responseCode = "201", description = "Time criado com sucesso")
    @PostMapping
    public ResponseEntity<Team> create(@Valid @RequestBody Team team) {
        log.info("criando um novo time: {}", team);
        Team created = teamService.save(team);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @Operation(summary = "Atualizar time", description = "Atualiza os dados de um time existente.")
    @ApiResponse(responseCode = "200", description = "Time atualizado com sucesso")
    @PostMapping("{id}")
    public Team update(@PathVariable Long id, @Valid @RequestBody Team team) {
        log.info("atualizando time com id: {}", id);
        return teamService.update(id, team);
    }

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
