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

import br.com.fiap.knowball.model.Referee;
import br.com.fiap.knowball.service.RefereeService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Tag(name = "Referee", description = "Endpoints de gerenciamento de árbitros")
@RestController
@RequestMapping("referees")
@RequiredArgsConstructor
@Slf4j
public class RefereeController {
    
    private final RefereeService refereeService;

    @Operation(summary = "Listar todos os árbitros", description = "Retorna a lista de todos os árbitros cadastrados.")
    @ApiResponse(responseCode = "200", description = "Lista de árbitros retornada com sucesso")
    @GetMapping
    public List<Referee> getAll() {
        log.info("buscando todos os árbitros");
        return refereeService.findAll();
    }

    @Operation(summary = "Buscar árbitro por ID", description = "Retorna um árbitro pelo seu identificador único.")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Árbitro encontrado"),
        @ApiResponse(responseCode = "404", description = "Árbitro não encontrado")
    })
    @GetMapping("{id}")
    public Referee getById(@PathVariable Long id) {
        log.info("buscando árbitro pelo id: {}", id);
        return refereeService.findById(id);
    }

    @Operation(summary = "Criar novo árbitro", description = "Cria um novo árbitro com os dados informados.")
    @ApiResponse(responseCode = "201", description = "Árbitro criado com sucesso")
    @PostMapping
    public ResponseEntity<Referee> create(@Valid @RequestBody Referee referee) {
        log.info("criando novo árbitro: {}", referee.getName());
        Referee created = refereeService.save(referee);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @Operation(summary = "Atualizar árbitro", description = "Atualiza os dados de um árbitro existente.")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Árbitro atualizado com sucesso"),
        @ApiResponse(responseCode = "404", description = "Árbitro não encontrado")
    })
    @PutMapping
    public Referee update(@PathVariable Long id, @Valid @RequestBody Referee referee) {
        log.info("atualizando árbitro com id: {}", id);
        return refereeService.update(id, referee);
    }

    @Operation(summary = "Deletar árbitro", description = "Remove um árbitro pelo seu ID.")
    @ApiResponses({
        @ApiResponse(responseCode = "204", description = "Árbitro deletado com sucesso"),
        @ApiResponse(responseCode = "404", description = "Árbitro não encontrado")
    })
    @DeleteMapping("{id}")
    public ResponseEntity<Void> destroy(@PathVariable Long id) {
        log.info("deletando árbitro com id: {}", id);
        refereeService.destroy(id);
        return ResponseEntity.noContent().build();
    }
}
