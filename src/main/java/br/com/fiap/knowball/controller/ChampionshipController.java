package br.com.fiap.knowball.controller;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
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

import br.com.fiap.knowball.assembler.ChampionshipModelAssembler;
import br.com.fiap.knowball.model.Championship;
import br.com.fiap.knowball.service.ChampionshipService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Tag(name = "Championship", description = "Endpoints de gerenciamento de campeonatos de futebol")
@RestController
@RequestMapping("championships")
@RequiredArgsConstructor
@Slf4j
public class ChampionshipController {

    private final ChampionshipService championshipService;
    private final ChampionshipModelAssembler assembler;

    @SuppressWarnings("null")
    @Operation(summary = "Listar todos os campeonatos", description = "Retorna todos os campeonatos cadastrados.")
    @ApiResponse(responseCode = "200", description = "Lista de campeonatos retornada com sucesso")
    @GetMapping
    public CollectionModel<EntityModel<Championship>> getAll() {
        log.info("buscando todos os campeonatos");
        List<EntityModel<Championship>> championships = championshipService.findAll().stream()
            .map(assembler::toModel)
            .collect(Collectors.toList());

        return CollectionModel.of(championships,
            linkTo(methodOn(ChampionshipController.class).getAll()).withSelfRel()
        );
    }

    @SuppressWarnings("null")
    @Operation(summary = "Buscar campeonato por ID", description = "Retorna um campeonato pelo seu identificador único.")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Campeonato encontrado"),
        @ApiResponse(responseCode = "404", description = "Campeonato não encontrado")
    })
    @GetMapping("{id}")
    public EntityModel<Championship> getById(@PathVariable Long id) {
        log.info("buscando campeonato por id: {}", id);
        Championship championship = championshipService.findById(id);
        return assembler.toModel(championship);
    }

    @SuppressWarnings("null")
    @Operation(summary = "Criar novo campeonato", description = "Cria um novo campeonato com os dados enviados.")
    @ApiResponse(responseCode = "201", description = "Campeonato criado com sucesso")
    @PostMapping
    public ResponseEntity<EntityModel<Championship>> create(@Valid @RequestBody Championship championship) {
        log.info("criando um novo campeonato: {}", championship.getName());
        Championship created = championshipService.save(championship);
        EntityModel<Championship> entityModel = assembler.toModel(created);

        return ResponseEntity.status(HttpStatus.CREATED).body(entityModel);
    }

    @SuppressWarnings("null")
    @Operation(summary = "Atualizar campeonato", description = "Atualiza os dados de um campeonato existente.")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Campeonato atualizado com sucesso"),
        @ApiResponse(responseCode = "404", description = "Campeonato não encontrado")
    })
    @PutMapping("{id}")
    public EntityModel<Championship> update(@PathVariable Long id, @Valid @RequestBody Championship championship) {
        log.info("atualizando campeonanto com id:{}", id);
        Championship updated = championshipService.update(id, championship);
        return assembler.toModel(updated);
    }

    @Operation(summary = "Deletar campeonato", description = "Remove um campeonato pelo seu ID.")
    @ApiResponses({
        @ApiResponse(responseCode = "204", description = "Campeonato deletado com sucesso"),
        @ApiResponse(responseCode = "404", description = "Campeonato não encontrado")
    })
    @DeleteMapping("{id}")
    public ResponseEntity<Void> destroy(@PathVariable Long id) {
        log.info("deletando campeonato com id: {}", id);
        championshipService.destroy(id);
        return ResponseEntity.noContent().build();
    }
}
