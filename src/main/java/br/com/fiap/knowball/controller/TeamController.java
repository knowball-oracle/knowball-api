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
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("teams")
@RequiredArgsConstructor
@Slf4j
public class TeamController {
    
    private final TeamService teamService;

    @GetMapping
    public List<Team> getAll() {
        log.info("buscando todos os times");
        return teamService.findAll();
    }

    @GetMapping("{id}")
    public Team getById(@PathVariable Long id) {
        log.info("buscando time por id: {}", id);
        return teamService.findById(id);
    }

    @PostMapping
    public ResponseEntity<Team> create(@Valid @RequestBody Team team) {
        log.info("criando um novo time: {}", team);
        Team created = teamService.save(team);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @PostMapping("{id}")
    public Team update(@PathVariable Long id, @Valid @RequestBody Team team) {
        log.info("atualizando time com id: {}", id);
        return teamService.update(id, team);
    }

    @DeleteMapping("{id}")
    public ResponseEntity<Void> destroy(@PathVariable Long id) {
        log.info("deletando time com id: {}", id);
        teamService.destroy(id);
        return ResponseEntity.noContent().build();
    }
}
