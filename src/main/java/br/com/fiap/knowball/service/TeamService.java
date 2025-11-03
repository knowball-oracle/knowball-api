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

import br.com.fiap.knowball.model.Team;
import br.com.fiap.knowball.repository.TeamRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class TeamService {
    
    private final TeamRepository teamRepository;
    private final DataSource dataSource;

    public TeamService(TeamRepository teamRepository, DataSource dataSource) {
        this.teamRepository = teamRepository;
        this.dataSource = dataSource;
    }

    public List<Team> findAll() {
        return teamRepository.findAll();
    }

    public Team findById(Long id) {
        return teamRepository
            .findById(id)
            .orElseThrow(() -> new EntityNotFoundException("Time não encontrado com id " + id));
    }

    public Team save(Team team) {

        //Executa a procedure de INSERT
        String sql = "{call prc_insert_equipe(?, ?, ?)}";
        try (Connection conn = dataSource.getConnection();
             CallableStatement stmt = conn.prepareCall(sql)) {
            
            stmt.setString(1, team.getName());   
            stmt.setString(2, team.getCity());   
            stmt.setString(3, team.getState());  
            stmt.execute();
            
            log.info("Team inserido via procedure: {}", team.getName());
        } catch (SQLException e) {
            log.error("Erro ao executar prc_insert_equipe: {}", e.getMessage());
            throw new ResponseStatusException(
                HttpStatus.BAD_REQUEST,
                "Erro ao inserir equipe via procedure: " + e.getMessage()
            );
        }

        return teamRepository.save(team);
    }

    public Team update(Long id, Team updatedTeam) {
        Team team = findById(id);

        // Executa a procedure de UPDATE
        String sql = "{call prc_update_equipe(?, ?, ?, ?)}";
        try (Connection conn = dataSource.getConnection();
             CallableStatement stmt = conn.prepareCall(sql)) {
            
            stmt.setLong(1, id);                 
            stmt.setString(2, updatedTeam.getName()); 
            stmt.setString(3, updatedTeam.getCity()); 
            stmt.setString(4, updatedTeam.getState());  
            stmt.execute();
            
            log.info("Team {} atualizado via procedure", id);
        } catch (SQLException e) {
            log.error("Erro ao executar prc_update_equipe: {}", e.getMessage());
            throw new ResponseStatusException(
                HttpStatus.BAD_REQUEST,
                "Erro ao atualizar equipe via procedure: " + e.getMessage()
            );
        }

        BeanUtils.copyProperties(updatedTeam, team, "id");
        return teamRepository.save(team);
    }

    public void destroy(Long id) {
        findById(id);

        // Executa a procedure de DELETE
        String sql = "{call prc_delete_equipe(?)}";
        try (Connection conn = dataSource.getConnection();
             CallableStatement stmt = conn.prepareCall(sql)) {
            
            stmt.setLong(1, id);  // p_id_equipe
            stmt.execute();
            
            log.info("Team {} deletado via procedure", id);
        } catch (SQLException e) {
            log.error("Erro ao executar prc_delete_equipe: {}", e.getMessage());
            throw new ResponseStatusException(
                HttpStatus.BAD_REQUEST,
                "Erro ao deletar equipe via procedure: " + e.getMessage()
            );
        }
    }
}
