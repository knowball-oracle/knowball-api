package br.com.fiap.knowball.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.fiap.knowball.model.Match;
import br.com.fiap.knowball.service.MatchService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("matches")
@RequiredArgsConstructor
@Slf4j
public class MatchController {
    
    private final MatchService matchService;

    @GetMapping
    public List<Match> getAll() {
        log.info("buscando todas as partidas");
        return matchService.findAll();
    }

    @GetMapping("{id}")
    public Match getById(@PathVariable Long id) {
        log.info("buscando partida pelo id: {}", id);
        return matchService.findById(id);
    }

    @GetMapping("/championship/{championshipId}")
    public List<Match> getByChampionship(@PathVariable Long championshipId) {
        log.info("buscando partidas pelo campeonato id: {}", championshipId);
        return matchService.findByChampionshipId(championshipId);
    }

    public ResponseEntity<Match> create(@Valid @RequestBody Match match) {
        log.info("criando nova partida na data {}", match.getDate());
        Match created = matchService.save(match);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    public Match update(@PathVariable Long id, @Valid @RequestBody Match match) {
        log.info("atualizando partida com id: {}", id);
        return matchService.update(id, match);
    }

    @DeleteMapping("{id}")
    public ResponseEntity<Void> destroy(@PathVariable Long id) {
        log.info("deletando partida com id: {}", id);
        matchService.destroy(id);
        return ResponseEntity.noContent().build();
    }
}
