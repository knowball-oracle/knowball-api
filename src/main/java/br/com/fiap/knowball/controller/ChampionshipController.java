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

import br.com.fiap.knowball.model.Championship;
import br.com.fiap.knowball.service.ChampionshipService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("championships")
@RequiredArgsConstructor
@Slf4j
public class ChampionshipController {

    private final ChampionshipService championshipService;

    @GetMapping
    public List<Championship> getAll() {
        log.info("buscando todos os campeonatos");
        return championshipService.findAll();
    }

    @GetMapping("{id}")
    public Championship getById(@PathVariable Long id) {
        log.info("buscando campeonato por id: {}", id);
        return championshipService.findById(id);
    }

    @PostMapping
    public ResponseEntity<Championship> create(@Valid @RequestBody Championship championship) {
        log.info("criando um novo campeonato: {}", championship.getName());
        Championship created = championshipService.save(championship);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @PutMapping("{id}")
    public Championship update(@PathVariable Long id, @Valid @RequestBody Championship championship) {
        log.info("atualizando campeonanto com id:{}", id);
        return championshipService.update(id, championship);
    }

    @DeleteMapping("{id}")
    public ResponseEntity<Void> destroy(@PathVariable Long id) {
        log.info("deletando campeonato com id: {}", id);
        championshipService.destroy(id);
        return ResponseEntity.noContent().build();
    }
}
