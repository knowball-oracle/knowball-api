package br.com.fiap.knowball.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import br.com.fiap.knowball.model.Championship;
import br.com.fiap.knowball.model.ChampionshipCategoryType;

public interface ChampionshipRepository extends JpaRepository<Championship, Long>{

    Optional<Championship> findByNameAndCategoryAndYear(String name, ChampionshipCategoryType category, Integer year);
}
