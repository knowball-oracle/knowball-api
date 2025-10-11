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

    @Operation(summary = "Listar todas as participações", description = "Retorna todas as participações cadastradas.")
    @ApiResponse(responseCode = "200", description = "Lista de participações retornada com sucesso")
    @GetMapping
    public List<Participation> getAll() {
        log.info("buscando todas as participações");
        return participationService.findAll();
    }

    @Operation(summary = "Listar participações por partida", description = "Retorna todas as participações de uma partida específica.")
    @ApiResponse(responseCode = "200", description = "Lista de participações da partida retornada com sucesso")
    @GetMapping("/match/{matchId}")
    public List<Participation> getByMatch(@PathVariable Long matchId) {
        log.info("buscando participações pela partida id: {}", matchId);
        return participationService.findByMatchId(matchId);
    }

    @Operation(summary = "Criar participação", description = "Cria uma nova participação para uma partida e time.")
    @ApiResponse(responseCode = "201", description = "Participação criada com sucesso")
    @PostMapping
    public ResponseEntity<Participation> create(@Valid @RequestBody Participation participation) {
        log.info("criando nova participação para partida id {} e time id {}", participation.getGame().getId(), participation.getTeam().getId());
        Participation created = participationService.save(participation);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @Operation(summary = "Atualizar participação", description = "Atualiza os dados de uma participação existente.")
    @ApiResponse(responseCode = "200", description = "Participação atualizada com sucesso")
    @PutMapping
    public Participation update(@Valid @RequestBody Participation participation) {
        log.info("atualizando participação para partida id {} e time id {}", participation.getGame().getId(), participation.getTeam().getId());
        return participationService.update(participation);
    }

    @Operation(summary = "Deletar participação", description = "Remove uma participação de uma partida e time específicos.")
    @ApiResponse(responseCode = "204", description = "Participação deletada com sucesso")
    @DeleteMapping("/match/{matchId}/team/{teamId}")
    public ResponseEntity<Void> destroy(@PathVariable Long matchId, @PathVariable Long teamId) {
        log.info("deletando participação para partida id {} e time id {}", matchId, teamId);
        participationService.deleteById(matchId, teamId);
        return ResponseEntity.noContent().build();
    }
}
