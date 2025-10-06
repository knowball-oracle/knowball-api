package br.com.fiap.knowball.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import br.com.fiap.knowball.model.Referee;

public interface RefereeRepository extends JpaRepository<Referee, Long>{
    Optional<Referee> findByName(String name);
}
