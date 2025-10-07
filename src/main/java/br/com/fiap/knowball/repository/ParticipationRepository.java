package br.com.fiap.knowball.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import br.com.fiap.knowball.model.Participation;
import br.com.fiap.knowball.model.ParticipationId;

public interface ParticipationRepository extends JpaRepository<Participation, ParticipationId>{

    @Query("SELECT p FROM Participation p WHERE p.match.id = :matchId")
    List<Participation> findByMatchId(Long matchId);
    
    List<Participation> findByTeamId(Long teamId);
}
