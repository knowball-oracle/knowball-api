package br.com.fiap.knowball.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import br.com.fiap.knowball.model.Match;
import java.util.List;


public interface MatchRepository extends JpaRepository<Match, Long>{
    List<Match> findByChampionshipId(Long championshipId);
}
