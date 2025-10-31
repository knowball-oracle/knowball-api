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

import br.com.fiap.knowball.assembler.ParticipationModelAssembler;
import br.com.fiap.knowball.model.Participation;
import br.com.fiap.knowball.service.ParticipationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Tag(name = "Participation", description = "Endpoints de gerenciamento de participações nas partidas")
@RestController
@RequestMapping("/participations")
@RequiredArgsConstructor
@Slf4j
public class ParticipationController {
    
    private final ParticipationService participationService;
    private final ParticipationModelAssembler assembler;

    @Operation(summary = "Listar todas as participações", description = "Retorna todas as participações cadastradas.")
    @ApiResponse(responseCode = "200", description = "Lista de participações retornada com sucesso")
    @GetMapping
    public CollectionModel<EntityModel<Participation>> getAll() {
        log.info("buscando todas as participações");
        List<EntityModel<Participation>> participations = participationService.findAll().stream()
            .map(assembler::toModel)
            .collect(Collectors.toList());

        return CollectionModel.of(participations,
            linkTo(methodOn(ParticipationController.class).getAll()).withSelfRel()
        );
    }

    @Operation(summary = "Listar participações por partida", description = "Retorna todas as participações de uma partida específica.")
    @ApiResponse(responseCode = "200", description = "Lista de participações da partida retornada com sucesso")
    @GetMapping("/game/{gameId}")
    public CollectionModel<EntityModel<Participation>> getByGame(@PathVariable Long gameId) {
        log.info("buscando participações pela partida id: {}", gameId);
        List<EntityModel<Participation>> participations = participationService.findByGameId(gameId).stream()
            .map(assembler::toModel)
            .collect(Collectors.toList());

        return CollectionModel.of(participations,
            linkTo(methodOn(ParticipationController.class).getByGame(gameId))
                .withSelfRel()
                .withTitle("List participations by game"),
            linkTo(methodOn(GameController.class).getById(gameId))
                .withRel("game")
                .withTitle("View game details"),
            linkTo(methodOn(ParticipationController.class).getAll())
                .withRel("all-participations")
                .withTitle("List all participations")
        );
    }

    @Operation(summary = "Criar participação", description = "Cria uma nova participação para uma partida e time.")
    @ApiResponse(responseCode = "201", description = "Participação criada com sucesso")
    @PostMapping
    public ResponseEntity<EntityModel<Participation>> create(@Valid @RequestBody Participation participation) {
        log.info("criando nova participação para partida id {} e time id {}", participation.getGame().getId(), participation.getTeam().getId());
        Participation created = participationService.save(participation);
        EntityModel<Participation> entityModel = assembler.toModel(created);

        return ResponseEntity
            .created(linkTo(methodOn(ParticipationController.class)
                .getByGame(created.getGame().getId())).toUri())
            .body(entityModel);
    }

    @Operation(summary = "Atualizar participação", description = "Atualiza os dados de uma participação existente.")
    @ApiResponse(responseCode = "200", description = "Participação atualizada com sucesso")
    @PutMapping
    public EntityModel<Participation> update(@Valid @RequestBody Participation participation) {
        log.info("atualizando participação para partida id {} e time id {}", participation.getGame().getId(), participation.getTeam().getId());
        Participation updated = participationService.update(participation);
        return assembler.toModel(updated);
    }

    @Operation(summary = "Deletar participação", description = "Remove uma participação de uma partida e time específicos.")
    @ApiResponse(responseCode = "204", description = "Participação deletada com sucesso")
    @DeleteMapping("/game/{gameId}/team/{gameId}")
    public ResponseEntity<Void> destroy(@PathVariable Long gameId, @PathVariable Long teamId) {
        log.info("deletando participação para partida id {} e time id {}", gameId, teamId);
        participationService.deleteById(gameId, teamId);
        return ResponseEntity.noContent().build();
    }
}
