package br.com.fiap.knowball.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import br.com.fiap.knowball.model.Participation;
import br.com.fiap.knowball.model.ParticipationId;

public interface ParticipationRepository extends JpaRepository<Participation, ParticipationId>{
    List<Participation> findByMatchId(Long matchId);
    List<Participation> findByTeamId(Long teamId);
}
