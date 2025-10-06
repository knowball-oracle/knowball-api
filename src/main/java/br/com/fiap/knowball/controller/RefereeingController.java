package br.com.fiap.knowball.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.fiap.knowball.model.Refereeing;
import br.com.fiap.knowball.service.RefereeingService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/refereeing")
@RequiredArgsConstructor
@Slf4j
public class RefereeingController {
    
    private final RefereeingService refereeingService;

    @GetMapping
    public List<Refereeing> getAll() {
        log.info("buscando todos os papéis de arbitragem");
        return refereeingService.findAll();
    }

    @GetMapping("/match/{matchId}")
    public List<Refereeing> getByMatchId(@PathVariable Long matchId) {
        log.info("buscando papéis de arbitragem pela partida id: {}", matchId);
        return refereeingService.findByMatchId(matchId);
    }

    @PostMapping
    public ResponseEntity<Refereeing> create(@Valid @RequestBody Refereeing refereeing) {
        log.info("criando novo papel de arbitragem para partida id {} e árbitro id {}", refereeing.getMatch().getId(), refereeing.getReferee().getId());
        Refereeing created = refereeingService.save(refereeing);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }
}
