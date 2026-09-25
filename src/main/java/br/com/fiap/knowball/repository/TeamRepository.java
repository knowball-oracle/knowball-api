package br.com.fiap.knowball.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import br.com.fiap.knowball.model.Team;

public interface TeamRepository extends JpaRepository<Team, Long>{

    Optional<Team> findByName(String name);

    List<Team> findAllByLogoUrlIsNull();
}
