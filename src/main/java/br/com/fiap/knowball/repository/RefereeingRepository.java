package br.com.fiap.knowball.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import br.com.fiap.knowball.model.Refereeing;
import br.com.fiap.knowball.model.RefereeingId;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

public interface RefereeingRepository extends JpaRepository<Refereeing, RefereeingId>{
    List<Refereeing> findByGameId(Long gameId);
    List<Refereeing> findByRefereeId(Long refereeId);

    @Modifying
    @Transactional
    @Query("DELETE FROM Refereeing r WHERE r.id.gameId = :gameId")
    void deleteByGameId(@Param("gameId") Long gameId);
}
