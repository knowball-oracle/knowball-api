package br.com.fiap.knowball.service;

import java.util.List;

import org.springframework.stereotype.Service;

import br.com.fiap.knowball.model.Refereeing;
import br.com.fiap.knowball.model.RefereeingId;
import br.com.fiap.knowball.repository.GameRepository;
import br.com.fiap.knowball.repository.RefereeRepository;
import br.com.fiap.knowball.repository.RefereeingRepository;
import jakarta.persistence.EntityNotFoundException;

@Service
public class RefereeingService {

    private final RefereeingRepository refereeingRepository;
    private final GameRepository matchRepository;
    private final RefereeRepository refereeRepository;

    public RefereeingService(RefereeingRepository refereeingRepository,
            GameRepository matchRepository,
            RefereeRepository refereeRepository) {
        this.refereeingRepository = refereeingRepository;
        this.matchRepository = matchRepository;
        this.refereeRepository = refereeRepository;
    }

    public List<Refereeing> findAll() {
        return refereeingRepository.findAll();
    }

    public List<Refereeing> findByGameId(Long gameId) {
        return refereeingRepository.findByGameId(gameId);
    }

    public Refereeing save(Refereeing refereeing) {

        matchRepository.findById(refereeing.getGame().getId())
                .orElseThrow(() -> new EntityNotFoundException("Partida não encontrada"));

        // Valida se árbitro existe
        refereeRepository.findById(refereeing.getReferee().getId())
                .orElseThrow(() -> new EntityNotFoundException("Árbitro não encontrado"));

        return refereeingRepository.save(refereeing);
    }

    public Refereeing findById(Long gameId, Long refereeId) {
        RefereeingId id = new RefereeingId(gameId, refereeId);

        return refereeingRepository.findById(id)

                .orElseThrow(() -> new EntityNotFoundException(
                        "Arbitragem não encontrada com gameId: " + gameId + " e refereeId: " + refereeId));
    }
}