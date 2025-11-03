package br.com.fiap.knowball.service;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

import javax.sql.DataSource;

import org.springframework.beans.BeanUtils;
import org.springframework.http.HttpStatus;
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
    private final DataSource dataSource;

    public ChampionshipService(
            ChampionshipRepository championshipRepository,
            GameRepository gameRepository,
            DataSource dataSource) {
        this.championshipRepository = championshipRepository;
        this.gameRepository = gameRepository;
        this.dataSource = dataSource;
    }

    public List<Championship> findAll() {
        return championshipRepository.findAll();
    }

    public Championship findById(Long id) {
        return championshipRepository
                .findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Campeonato não encontrado com id " + id));
    }

    public Championship save(Championship championship) {

        // Executa a procedure de INSERT
        String sql = "{call prc_insert_campeonato(?, ?, ?)}";
        try (Connection conn = dataSource.getConnection();
                CallableStatement stmt = conn.prepareCall(sql)) {

            stmt.setString(1, championship.getName());
            stmt.setString(2, championship.getCategory().name());
            stmt.setInt(3, championship.getYear());
            stmt.execute();

            log.info("Championship inserido via procedure: {}", championship.getName());
        } catch (SQLException e) {
            log.error("Erro ao executar prc_insert_campeonato {}", e.getMessage());
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Erro ao inserir campeonato via procedure: " + e.getMessage());
        }

        return championshipRepository.save(championship);
    }

    public Championship update(Long id, Championship updatedChampionship) {
        Championship championship = findById(id);

        // Executa a procedure de UPDATE
        String sql = "{call prc_update_campeonato(?, ?, ?, ?)}";
        try (Connection conn = dataSource.getConnection();
                CallableStatement stmt = conn.prepareCall(sql)) {

            stmt.setLong(1, id);
            stmt.setString(2, updatedChampionship.getName());
            stmt.setString(3, updatedChampionship.getCategory().name());
            stmt.setInt(4, updatedChampionship.getYear());
            stmt.execute();

            log.info("Championship {} atualizado via procedure", id);
        } catch (SQLException e) {
            log.error("Erro ao executar prc_update_campeonato: {}", e.getMessage());
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

        // Executa a procedure de DELETE
        String sql = "{call prc_delete_campeonato(?)}";
        try (Connection conn = dataSource.getConnection();
                CallableStatement stmt = conn.prepareCall(sql)) {

            stmt.setLong(1, id);
            stmt.execute();

            log.info("Championship {} deletado via procedure", id);
        } catch (SQLException e) {
            log.error("Erro ao executar prc_delete_campeoanto: {}", e.getMessage());
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Erro ao deleter campeonato via procedure: " + e.getMessage());
        }
    }
}
