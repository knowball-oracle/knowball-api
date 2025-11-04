package br.com.fiap.knowball.service;

import java.util.List;

import org.springframework.beans.BeanUtils;
import org.springframework.http.HttpStatus;
import org.springframework.lang.NonNull;
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

    public TeamService(TeamRepository teamRepository) {
        this.teamRepository = teamRepository;
    }

    public List<Team> findAll() {
        return teamRepository.findAll();
    }

    public Team findById(@NonNull Long id) {
        return teamRepository
            .findById(id)
            .orElseThrow(() -> new EntityNotFoundException("Time não encontrado com id " + id));
    }

    public Team save(Team team) {

        try {
            teamRepository.insertTeam(
                team.getName(),
                team.getCity(),
                team.getState()
            );
            log.info("Team inserido via procedure: {}", team.getName());
        } catch (Exception e) {
            log.error("Erro ao inserir time via procedure: {}", e.getMessage());
            throw new ResponseStatusException(
                HttpStatus.BAD_REQUEST,
                "Erro ao inserir time via procedure: " + e.getMessage());
        }
        return team;
    }

    public Team update(@NonNull Long id, Team updatedTeam) {
        Team team = findById(id);

        try {
            teamRepository.updateTeam(
                id,
                updatedTeam.getName(),
                updatedTeam.getCity(),
                updatedTeam.getState()
            );
            log.info("Team {} atualizado via procedure", id);
        } catch (Exception e) {
            log.error("Erro ao atualizar time via procedure: {}", e.getMessage());
            throw new ResponseStatusException(
                HttpStatus.BAD_REQUEST,
                "Erro ao atualizar time via procedure: " + e.getMessage());
        }
        BeanUtils.copyProperties(updatedTeam, team, "id");
        return teamRepository.save(team);
    }

    public void destroy(@NonNull Long id) {
        findById(id);

        try {
            teamRepository.deleteTeam(id);
            log.info("Team {} deletado via procedure", id);
        } catch (Exception e) {
            throw new ResponseStatusException(
                HttpStatus.BAD_REQUEST,
                "Erro ao deleter equipe via procedure: " + e.getMessage());
        }
    }
}
