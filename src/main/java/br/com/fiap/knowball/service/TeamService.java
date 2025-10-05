package br.com.fiap.knowball.service;

import java.util.List;

import org.springframework.beans.BeanUtils;

import br.com.fiap.knowball.model.Team;
import br.com.fiap.knowball.repository.TeamRepository;
import jakarta.persistence.EntityNotFoundException;

public class TeamService {
    
    private final TeamRepository teamRepository;

    public TeamService(TeamRepository teamRepository) {
        this.teamRepository = teamRepository;
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
        return teamRepository.save(team);
    }

    public Team update(Long id, Team updatedTeam) {
        Team team = findById(id);
        BeanUtils.copyProperties(updatedTeam, team, "id");
        return teamRepository.save(team);
    }

    public void destroy(Long id) {
        teamRepository.deleteById(id);
    }
}
