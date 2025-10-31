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

import br.com.fiap.knowball.assembler.RefereeingModelAssembler;
import br.com.fiap.knowball.model.Refereeing;
import br.com.fiap.knowball.service.RefereeingService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Tag(name = "Refereeing", description = "Endpoints de gerenciamento de papéis de arbitragem")
@RestController
@RequestMapping("/refereeing")
@RequiredArgsConstructor
@Slf4j
public class RefereeingController {
    
    private final RefereeingService refereeingService;
    private final RefereeingModelAssembler assembler;

    @Operation(summary = "Listar todos os papéis de arbitragem", description = "Retorna todos os papéis de arbitragem cadastrados.")
    @ApiResponse(responseCode = "200", description = "Lista de papéis de arbitragem retornada com sucesso")
    @GetMapping
    public CollectionModel<EntityModel<Refereeing>> getAll() {
        log.info("buscando todos os papéis de arbitragem");
        List<EntityModel<Refereeing>> refereeing = refereeingService.findAll().stream()
            .map(assembler::toModel)
            .collect(Collectors.toList());

        return CollectionModel.of(refereeing,
            linkTo(methodOn(RefereeingController.class).getAll()).withSelfRel()
        );
    }

    @Operation(summary = "Listar papéis de arbitragem por partida", description = "Retorna todos os papéis de arbitragem para uma partida específica.")
    @ApiResponse(responseCode = "200", description = "Lista de papéis de arbitragem da partida retornada com sucesso")
    @GetMapping("/game/{gameId}")
    public CollectionModel<EntityModel<Refereeing>> getByGameId(@PathVariable Long gameId) {
        log.info("buscando papéis de arbitragem pela partida id: {}", gameId);
        List<EntityModel<Refereeing>> refereeing = refereeingService.findByGameId(gameId).stream()
            .map(assembler::toModel)
            .collect(Collectors.toList());

        return CollectionModel.of(refereeing,
            linkTo(methodOn(RefereeingController.class).getByGameId(gameId))
                .withSelfRel()
                .withTitle("List refereeing by game"),
            linkTo(methodOn(GameController.class).getById(gameId))
                .withRel("game")
                .withTitle("View game details"),
            linkTo(methodOn(RefereeingController.class).getAll())
                .withRel("all-refereeing")
                .withTitle("List all refereeing assignments")
        );
    }

    @Operation(summary = "Criar papel de arbitragem", description = "Cria um novo papel de arbitragem para uma partida e árbitro.")
    @ApiResponse(responseCode = "201", description = "Papel de arbitragem criado com sucesso")
    @PostMapping
    public ResponseEntity<EntityModel<Refereeing>> create(@Valid @RequestBody Refereeing refereeing) {
        log.info("criando novo papel de arbitragem para partida id {} e árbitro id {}", refereeing.getGame().getId(), refereeing.getReferee().getId());
        Refereeing created = refereeingService.save(refereeing);
        EntityModel<Refereeing> entityModel = assembler.toModel(created);

        return ResponseEntity
            .created(linkTo(methodOn(RefereeingController.class)
                .getByGameId(created.getGame().getId())).toUri())
            .body(entityModel);
    }
}
