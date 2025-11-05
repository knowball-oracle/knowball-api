package br.com.fiap.knowball.service;

import java.util.List;

import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import br.com.fiap.knowball.model.Game;
import br.com.fiap.knowball.repository.ChampionshipRepository;
import br.com.fiap.knowball.repository.GameRepository;
import jakarta.persistence.EntityNotFoundException;

@Service
public class GameService {
    
    private final GameRepository gameRepository;
    private final ChampionshipRepository championshipRepository;

    public GameService(GameRepository gameRepository, ChampionshipRepository championshipRepository) {
        this.gameRepository = gameRepository;
        this.championshipRepository = championshipRepository;
    }

    public List<Game> findAll() {
        return gameRepository.findAll();
    }

    @SuppressWarnings("null")
    public Game findById(Long id) {
        return gameRepository
            .findById(id)
            .orElseThrow(() -> new EntityNotFoundException("Partida não encontrada com id " + id));
    }

    @SuppressWarnings("null")
    public List<Game> findByChampionshipId(Long championshipId) {
        if(!championshipRepository.existsById(championshipId)) {
            throw new EntityNotFoundException("Campeonato não encontrado");
        } 

        return gameRepository.findByChampionshipId(championshipId);
    }

    @SuppressWarnings("null")
    public Game save(Game game) {
        championshipRepository.findById(game.getChampionship().getId())
            .orElseThrow(() -> new EntityNotFoundException("Campeonato não encontrado"));

        return gameRepository.save(game);
    }

    @SuppressWarnings("null")
    public Game update(Long id, Game updatedMatch) {
        Game game = findById(id);
        BeanUtils.copyProperties(updatedMatch, game, "id");
        return gameRepository.save(game);
    }

    @SuppressWarnings("null")
    public void destroy(Long id) {
        gameRepository.deleteById(id);
    }
}
