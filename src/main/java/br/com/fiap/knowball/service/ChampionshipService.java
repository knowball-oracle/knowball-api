package br.com.fiap.knowball.service;

import java.util.List;

import org.springframework.beans.BeanUtils;
import org.springframework.http.HttpStatus;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

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

    public Championship save(Championship championship) {

       try {
            championshipRepository.insertChampionship(
                championship.getName(),
                championship.getCategory().name(),
                championship.getYear()
        );
       } catch (Exception e) {
            log.error("Erro ao inserir campeonato via procedure: {}", e.getMessage());
            throw new ResponseStatusException(
                HttpStatus.BAD_REQUEST,
                "Erro ao inserir campeonato via procedure: " + e.getMessage());
       }
        return championship;
    }

    public Championship update(@NonNull Long id, Championship updatedChampionship) {
        Championship championship = findById(id);

        try {
            championshipRepository.updateChampionship(
                id,
                updatedChampionship.getName(),
                updatedChampionship.getCategory().name(),
                updatedChampionship.getYear()
            );
            log.info("Championship {} atualizado via procedure", id);
        } catch (Exception e) {
            log.error("Erro ao atualizar championship via procedure: {}", e.getMessage());
            throw new ResponseStatusException(
                HttpStatus.BAD_REQUEST,
                "Erro ao atualizar campeonato via procedure: " + e.getMessage());
        }
        BeanUtils.copyProperties(updatedChampionship, championship, "id");
        return championshipRepository.save(championship);
    }

    public void destroy(Long id) {
        findById(id);

        List<Game> games = gameRepository.findByChampionshipId(id);
        gameRepository.deleteAll(games);

        try {
            championshipRepository.deleteChampionship(id);
            log.info("Championship {} deletado via procedure", id);
        } catch (Exception e) {
            log.error("Erro ao deleter campeonato via procedure: {}", e.getMessage());
            throw new ResponseStatusException(
                HttpStatus.BAD_REQUEST,
                "Erro ao deletar campeonato via procedure: " + e.getMessage());
        }
    }
}
