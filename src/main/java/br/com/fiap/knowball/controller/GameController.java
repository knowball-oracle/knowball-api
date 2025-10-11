package br.com.fiap.knowball.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.fiap.knowball.model.Game;
import br.com.fiap.knowball.service.GameService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Tag(name = "Game", description = "Endpoints de gerenciamento de partidas de futebol")
@RestController
@RequestMapping("games")
@RequiredArgsConstructor
@Slf4j
public class GameController {
    
    private final GameService matchService;

    @Operation(summary = "Buscar todas as partidas", description = "Retorna uma lista de todas as partidas cadastradas.")
    @ApiResponse(responseCode = "200", description = "Lista de partidas retornada com sucesso")
    @GetMapping
    public List<Game> getAll() {
        log.info("buscando todas as partidas");
        return matchService.findAll();
    }

    @Operation(summary = "Buscar partida por ID", description = "Busca uma partida específica pelo seu identificador único.")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Partida encontrada"),
        @ApiResponse(responseCode = "404", description = "Partida não encontrada")
    })
    @GetMapping("{id}")
    public Game getById(@PathVariable Long id) {
        log.info("buscando partida pelo id: {}", id);
        return matchService.findById(id);
    }

    @GetMapping("/championship/{championshipId}")
    public List<Game> getByChampionship(@PathVariable Long championshipId) {
        log.info("buscando partidas pelo campeonato id: {}", championshipId);
        return matchService.findByChampionshipId(championshipId);
    }

    @Operation(summary = "Criar nova partida", description = "Cria uma nova partida com os dados informados.")
    @ApiResponse(responseCode = "201", description = "Partida criada com sucesso")
    @PostMapping
    public ResponseEntity<Game> create(@Valid @RequestBody Game match) {
        log.info("criando nova partida na data {}", match.getMatchDate());
        Game created = matchService.save(match);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @Operation(summary = "Atualizar partida por ID", description = "Atualiza os dados de uma partida existente.")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Partida atualizada com sucesso"),
        @ApiResponse(responseCode = "404", description = "Partida não encontrada")
    })
    @PutMapping("{id}")
    public Game update(@PathVariable Long id, @Valid @RequestBody Game match) {
        log.info("atualizando partida com id: {}", id);
        return matchService.update(id, match);
    }

    @Operation(summary = "Deletar partida por ID", description = "Exclui uma partida do sistema pelo seu ID.")
    @ApiResponses({
        @ApiResponse(responseCode = "204", description = "Partida deletada com sucesso"),
        @ApiResponse(responseCode = "404", description = "Partida não encontrada")
    })
    @DeleteMapping("{id}")
    public ResponseEntity<Void> destroy(@PathVariable Long id) {
        log.info("deletando partida com id: {}", id);
        matchService.destroy(id);
        return ResponseEntity.noContent().build();
    }
}
