package br.com.fiap.knowball.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import br.com.fiap.knowball.model.Refereeing;
import br.com.fiap.knowball.model.RefereeingId;

public interface RefereeingRepository extends JpaRepository<Refereeing, RefereeingId>{
    List<Refereeing> findByGameId(Long gameId);
    List<Refereeing> findByRefereeId(Long refereeId);
}
