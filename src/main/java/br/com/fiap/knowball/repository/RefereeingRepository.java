package br.com.fiap.knowball.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import br.com.fiap.knowball.model.RefeeringId;
import br.com.fiap.knowball.model.Refereeing;

public interface RefereeingRepository extends JpaRepository<Refereeing, RefeeringId>{
    List<Refereeing> findByMatchId(Long matchId);
    List<Refereeing> findByRefereeId(Long refereeId);
}
