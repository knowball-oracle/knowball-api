package br.com.fiap.knowball.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.fiap.knowball.model.Refereeing;
import br.com.fiap.knowball.repository.MatchRepository;
import br.com.fiap.knowball.repository.RefereeRepository;
import jakarta.persistence.EntityNotFoundException;

@Service
public class RefereeingService {
    
    @Autowired
    private final RefereeingService refereeingRepository;
    private final MatchRepository matchRepository;
    private final RefereeRepository refereeRepository;

    public RefereeingService(RefereeingService refereeingRepository,
                             MatchRepository matchRepository,
                             RefereeRepository refereeRepository) {
        this.refereeingRepository = refereeingRepository;
        this.matchRepository = matchRepository;
        this.refereeRepository = refereeRepository;
    }

    public List<Refereeing> findAll() {
        return refereeingRepository.findAll();
    }

    public List<Refereeing> findByMatchId(Long matchId) {
        return refereeingRepository.findByMatchId(matchId);
    }

    public Refereeing save(Refereeing refereeing) {
        
        matchRepository.findById(refereeing.getMatch().getId())
            .orElseThrow(() -> new EntityNotFoundException("Partida não encontrada"));

        // Valida se árbitro existe
        refereeRepository.findById(refereeing.getReferee().getId())
            .orElseThrow(() -> new EntityNotFoundException("Árbitro não encontrado"));

        return refereeingRepository.save(refereeing);
    }
}