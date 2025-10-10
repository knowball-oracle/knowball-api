package br.com.fiap.knowball.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import br.com.fiap.knowball.model.Game;
import java.util.List;


public interface GameRepository extends JpaRepository<Game, Long>{

    @Query("SELECT m FROM Game m WHERE m.championship.id = :championshipId")
    List<Game> findByChampionshipId(@Param("championshipId") Long championshipId);
}
