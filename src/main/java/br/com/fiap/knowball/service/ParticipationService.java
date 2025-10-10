package br.com.fiap.knowball.service;

import java.util.List;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.fiap.knowball.model.Participation;
import br.com.fiap.knowball.model.ParticipationId;
import br.com.fiap.knowball.model.ParticipationType;
import br.com.fiap.knowball.repository.GameRepository;
import br.com.fiap.knowball.repository.ParticipationRepository;
import br.com.fiap.knowball.repository.TeamRepository;
import jakarta.persistence.EntityNotFoundException;

@Service
public class ParticipationService {
    
    @Autowired
    private final ParticipationRepository participationRepository;
    private final GameRepository matchRepository;
    private final TeamRepository teamRepository;

    public ParticipationService(ParticipationRepository participationRepository,
                                GameRepository matchRepository,
                                TeamRepository teamRepository) {
        this.participationRepository = participationRepository;
        this.matchRepository = matchRepository;
        this.teamRepository = teamRepository;
    }

    public List<Participation> findAll() {
        return participationRepository.findAll();
    }

    public List<Participation> findByMatchId(Long matchId) {
        if(!matchRepository.existsById(matchId)) {
            throw new EntityNotFoundException("Partida não encontrada com id " + matchId);
        }
        return participationRepository.findByGameId(matchId);
    }

    public Participation save(Participation participation) {
        Long matchId = participation.getGame().getId();
        Long teamId = participation.getTeam().getId();
        ParticipationType type = participation.getType();

        matchRepository.findById(matchId)
            .orElseThrow(() -> new EntityNotFoundException("Partida não encontrada"));

        teamRepository.findById(teamId)
            .orElseThrow(() -> new EntityNotFoundException("Time não encontrado"));

        boolean exists = participationRepository.findByGameId(matchId).stream()
            .anyMatch(p -> p.getType().equals(type));

        if (exists) {
            throw new IllegalArgumentException("Já existe uma equipe " + type + " cadastrada para esta partida");
        }

        return participationRepository.save(participation);
    }

    public Participation update(Participation participation) {
        Long matchId = participation.getGame().getId();
        Long teamId = participation.getTeam().getId();
        ParticipationType type = participation.getType();

        ParticipationId id = new ParticipationId(matchId, teamId);

        Participation existing = participationRepository.findById(id)
            .orElseThrow(() -> new EntityNotFoundException("Participação não encontrada"));

        if (!existing.getType().equals(type)) {
            boolean typeExists = participationRepository.findByGameId(matchId).stream()
                .anyMatch(p -> p.getType().equals(type));

            if (typeExists) {
                throw new IllegalArgumentException("Já existe uma equipe " + type + " cadastrada para esta partida.");
            }
        }

        BeanUtils.copyProperties(participation, existing, "id");
        return participationRepository.save(existing);
    }

     public void deleteById(Long matchId, Long teamId) {
        ParticipationId id = new ParticipationId(matchId, teamId);
        participationRepository.deleteById(id);
    }
    
}
