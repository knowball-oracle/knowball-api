package br.com.fiap.knowball.service;

import java.util.List;

import br.com.fiap.knowball.repository.RefereeingRepository;
import br.com.fiap.knowball.repository.ReportRepository;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import br.com.fiap.knowball.model.Game;
import br.com.fiap.knowball.repository.ChampionshipRepository;
import br.com.fiap.knowball.repository.GameRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.transaction.annotation.Transactional;

@Service
public class GameService {
    
    private final GameRepository gameRepository;
    private final ChampionshipRepository championshipRepository;
    private final ReportRepository reportRepository;
    private final RefereeingRepository refereeingRepository;

    public GameService(GameRepository gameRepository, ChampionshipRepository championshipRepository, ReportRepository reportRepository, RefereeingRepository refereeingRepository) {
        this.gameRepository = gameRepository;
        this.championshipRepository = championshipRepository;
        this.reportRepository = reportRepository;
        this.refereeingRepository = refereeingRepository;
    }

    public List<Game> findAll() {
        return gameRepository.findAll();
    }

    public Game findById(Long id) {
        return gameRepository
            .findById(id)
            .orElseThrow(() -> new EntityNotFoundException("Partida não encontrada com id " + id));
    }

    public List<Game> findByChampionshipId(Long championshipId) {
        if(!championshipRepository.existsById(championshipId)) {
            throw new EntityNotFoundException("Campeonato não encontrado");
        } 

        return gameRepository.findByChampionshipId(championshipId);
    }

    public Game save(Game game) {
        championshipRepository.findById(game.getChampionship().getId())
            .orElseThrow(() -> new EntityNotFoundException("Campeonato não encontrado"));

        return gameRepository.save(game);
    }

    public Game update(Long id, Game updatedMatch) {
        Game game = findById(id);
        BeanUtils.copyProperties(updatedMatch, game, "id");
        return gameRepository.save(game);
    }

    @Transactional
    public void destroy(Long id) {
        findById(id);
        reportRepository.deleteByGameId(id);
        refereeingRepository.deleteByGameId(id);
        gameRepository.deleteById(id);
    }
}
