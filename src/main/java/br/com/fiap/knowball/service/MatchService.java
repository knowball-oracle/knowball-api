package br.com.fiap.knowball.service;

import java.util.List;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.fiap.knowball.model.Match;
import br.com.fiap.knowball.repository.ChampionshipRepository;
import br.com.fiap.knowball.repository.MatchRepository;
import jakarta.persistence.EntityNotFoundException;

@Service
public class MatchService {
    
    @Autowired
    private final MatchRepository matchRepository;
    private final ChampionshipRepository championshipRepository;

    public MatchService(MatchRepository matchRepository, ChampionshipRepository championshipRepository) {
        this.matchRepository = matchRepository;
        this.championshipRepository = championshipRepository;
    }

    public List<Match> findAll() {
        return matchRepository.findAll();
    }

    public Match findById(Long id) {
        return matchRepository
            .findById(id)
            .orElseThrow(() -> new EntityNotFoundException("Partida não encontrada com id " + id));
    }

    public Match save(Match match) {
        championshipRepository.findById(match.getChampionship().getId())
            .orElseThrow(() -> new EntityNotFoundException("Campeonato não encontrado"));

        return matchRepository.save(match);
    }

    public Match update(Long id, Match updatedMatch) {
        Match match = findById(id);
        BeanUtils.copyProperties(updatedMatch, match, "id");
        return matchRepository.save(match);
    }

    public void destroy(Long id) {
        matchRepository.deleteById(id);
    }
}
