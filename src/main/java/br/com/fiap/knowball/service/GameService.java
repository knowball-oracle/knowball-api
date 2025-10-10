package br.com.fiap.knowball.service;

import java.util.List;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.fiap.knowball.model.Game;
import br.com.fiap.knowball.repository.ChampionshipRepository;
import br.com.fiap.knowball.repository.GameRepository;
import jakarta.persistence.EntityNotFoundException;

@Service
public class GameService {
    
    @Autowired
    private final GameRepository matchRepository;
    private final ChampionshipRepository championshipRepository;

    public GameService(GameRepository matchRepository, ChampionshipRepository championshipRepository) {
        this.matchRepository = matchRepository;
        this.championshipRepository = championshipRepository;
    }

    public List<Game> findAll() {
        return matchRepository.findAll();
    }

    public Game findById(Long id) {
        return matchRepository
            .findById(id)
            .orElseThrow(() -> new EntityNotFoundException("Partida não encontrada com id " + id));
    }

    public List<Game> findByChampionshipId(Long championshipId) {
        if(!championshipRepository.existsById(championshipId)) {
            throw new EntityNotFoundException("Campeonato não encontrado");
        } 

        return matchRepository.findByChampionshipId(championshipId);
    }

    public Game save(Game game) {
        championshipRepository.findById(game.getChampionship().getId())
            .orElseThrow(() -> new EntityNotFoundException("Campeonato não encontrado"));

        return matchRepository.save(game);
    }

    public Game update(Long id, Game updatedMatch) {
        Game game = findById(id);
        BeanUtils.copyProperties(updatedMatch, game, "id");
        return matchRepository.save(game);
    }

    public void destroy(Long id) {
        matchRepository.deleteById(id);
    }
}
