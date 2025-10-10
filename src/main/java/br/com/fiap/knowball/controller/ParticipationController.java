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
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/participations")
@RequiredArgsConstructor
@Slf4j
public class ParticipationController {
    
    private final ParticipationService participationService;

    @GetMapping
    public List<Participation> getAll() {
        log.info("buscando todas as participações");
        return participationService.findAll();
    }

    @GetMapping("/match/{matchId}")
    public List<Participation> getByMatch(@PathVariable Long matchId) {
        log.info("buscando participações pela partida id: {}", matchId);
        return participationService.findByMatchId(matchId);
    }

    @PostMapping
    public ResponseEntity<Participation> create(@Valid @RequestBody Participation participation) {
        log.info("criando nova participação para partida id {} e time id {}", participation.getGame().getId(), participation.getTeam().getId());
        Participation created = participationService.save(participation);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @PutMapping
    public Participation update(@Valid @RequestBody Participation participation) {
        log.info("atualizando participação para partida id {} e time id {}", participation.getGame().getId(), participation.getTeam().getId());
        return participationService.update(participation);
    }

    @DeleteMapping("/match/{matchId}/team/{teamId}")
    public ResponseEntity<Void> destroy(@PathVariable Long matchId, @PathVariable Long teamId) {
        log.info("deletando participação para partida id {} e time id {}", matchId, teamId);
        participationService.deleteById(matchId, teamId);
        return ResponseEntity.noContent().build();
    }
}
