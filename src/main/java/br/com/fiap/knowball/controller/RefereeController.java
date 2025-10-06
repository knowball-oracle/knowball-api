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
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("referees")
@RequiredArgsConstructor
@Slf4j
public class RefereeController {
    
    private final RefereeService refereeService;

    @GetMapping
    public List<Referee> getAll() {
        log.info("buscando todos os árbitros");
        return refereeService.findAll();
    }

    @GetMapping("{id}")
    public Referee getById(@PathVariable Long id) {
        log.info("buscando árbitro pelo id: {}", id);
        return refereeService.findById(id);
    }

    @PostMapping
    public ResponseEntity<Referee> create(@Valid @RequestBody Referee referee) {
        log.info("criando novo árbitro: {}", referee.getName());
        Referee created = refereeService.save(referee);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @PutMapping
    public Referee update(@PathVariable Long id, @Valid @RequestBody Referee referee) {
        log.info("atualizando árbitro com id: {}", id);
        return refereeService.update(id, referee);
    }

    @DeleteMapping("{id}")
    public ResponseEntity<Void> destroy(@PathVariable Long id) {
        log.info("deletando árbitro com id: {}", id);
        refereeService.destroy(id);
        return ResponseEntity.noContent().build();
    }
}
