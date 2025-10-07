package br.com.fiap.knowball.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import br.com.fiap.knowball.model.Match;
import java.util.List;


public interface MatchRepository extends JpaRepository<Match, Long>{

    @Query("SELECT m FROM Match m WHERE m.championship.id = :championshipId")
    List<Match> findByChampionshipId(@Param("championshipId") Long championshipId);
}
