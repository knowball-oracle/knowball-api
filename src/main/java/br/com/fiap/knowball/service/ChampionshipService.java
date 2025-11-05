package br.com.fiap.knowball.service;

import java.util.List;

import org.springframework.beans.BeanUtils;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;

import br.com.fiap.knowball.model.Championship;
import br.com.fiap.knowball.model.Game;
import br.com.fiap.knowball.repository.ChampionshipRepository;
import br.com.fiap.knowball.repository.GameRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class ChampionshipService {

    private final ChampionshipRepository championshipRepository;
    private final GameRepository gameRepository;

    public ChampionshipService(
            ChampionshipRepository championshipRepository,
            GameRepository gameRepository) {
        this.championshipRepository = championshipRepository;
        this.gameRepository = gameRepository;
    }

    public List<Championship> findAll() {
        return championshipRepository.findAll();
    }

    public Championship findById(@NonNull Long id) {
        return championshipRepository
                .findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Campeonato não encontrado com id " + id));
    }

    @SuppressWarnings("null")
    public Championship save(Championship championship) {
        return championshipRepository.save(championship);
    }

    @SuppressWarnings("null")
    public Championship update(@NonNull Long id, Championship updatedChampionship) {
        Championship championship = findById(id);
        BeanUtils.copyProperties(updatedChampionship, championship, "id");
        return championshipRepository.save(championship);
    }

    @SuppressWarnings("null")
    public void destroy(Long id) {
        Championship championship = findById(id);
        List<Game> games = gameRepository.findByChampionshipId(id);
        gameRepository.deleteAll(games);
        championshipRepository.delete(championship);
    }
}
