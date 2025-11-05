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

import br.com.fiap.knowball.assembler.GameModelAssembler;
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
    
    private final GameService gameService;
    private final GameModelAssembler assembler;

    @SuppressWarnings("null")
    @Operation(summary = "Buscar todas as partidas", description = "Retorna uma lista de todas as partidas cadastradas.")
    @ApiResponse(responseCode = "200", description = "Lista de partidas retornada com sucesso")
    @GetMapping
    public CollectionModel<EntityModel<Game>> getAll() {
        log.info("buscando todas as partidas");
        List<EntityModel<Game>> games = gameService.findAll().stream()
            .map(assembler::toModel)
            .collect(Collectors.toList());
        
        return CollectionModel.of(games,
            linkTo(methodOn(GameController.class).getAll()).withSelfRel()
        );
    }

    @SuppressWarnings("null")
    @Operation(summary = "Buscar partida por ID", description = "Busca uma partida específica pelo seu identificador único.")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Partida encontrada"),
        @ApiResponse(responseCode = "404", description = "Partida não encontrada")
    })
    @GetMapping("{id}")
    public EntityModel<Game> getById(@PathVariable Long id) {
        log.info("buscando partida pelo id: {}", id);
        Game game = gameService.findById(id);
        return assembler.toModel(game);
    }

    @SuppressWarnings("null")
    @GetMapping("/championship/{championshipId}")
    public CollectionModel<EntityModel<Game>> getByChampionship(@PathVariable Long championshipId) {
        log.info("buscando partidas pelo campeonato id: {}", championshipId);
        List<EntityModel<Game>> games = gameService.findByChampionshipId(championshipId).stream()
            .map(assembler::toModel)
            .collect(Collectors.toList());

        return CollectionModel.of(games,
            linkTo(methodOn(GameController.class).getByChampionship(championshipId))
                .withSelfRel()
                .withTitle("List games by championship"),
            linkTo(methodOn(ChampionshipController.class).getById(championshipId))
                .withRel("championship")
                .withTitle("View championship details"),
            linkTo(methodOn(GameController.class).getAll())
                .withRel("all-games")
                .withTitle("List all games")
        );
    }

    @SuppressWarnings("null")
    @Operation(summary = "Criar nova partida", description = "Cria uma nova partida com os dados informados.")
    @ApiResponse(responseCode = "201", description = "Partida criada com sucesso")
    @PostMapping
    public ResponseEntity<EntityModel<Game>> create(@Valid @RequestBody Game game) {
        log.info("criando nova partida na data {}", game.getMatchDate());
        Game created = gameService.save(game);
        EntityModel<Game> entityModel = assembler.toModel(created);

        return ResponseEntity
            .created(linkTo(methodOn(GameController.class).getById(created.getId())).toUri())
            .body(entityModel);
    }

    @SuppressWarnings("null")
    @Operation(summary = "Atualizar partida por ID", description = "Atualiza os dados de uma partida existente.")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Partida atualizada com sucesso"),
        @ApiResponse(responseCode = "404", description = "Partida não encontrada")
    })
    @PutMapping("{id}")
    public EntityModel<Game> update(@PathVariable Long id, @Valid @RequestBody Game game) {
        log.info("atualizando partida com id: {}", id);
        Game updated = gameService.update(id, game);
        return assembler.toModel(updated);
    }

    @Operation(summary = "Deletar partida por ID", description = "Exclui uma partida do sistema pelo seu ID.")
    @ApiResponses({
        @ApiResponse(responseCode = "204", description = "Partida deletada com sucesso"),
        @ApiResponse(responseCode = "404", description = "Partida não encontrada")
    })
    @DeleteMapping("{id}")
    public ResponseEntity<Void> destroy(@PathVariable Long id) {
        log.info("deletando partida com id: {}", id);
        gameService.destroy(id);
        return ResponseEntity.noContent().build();
    }
}
