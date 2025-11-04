package br.com.fiap.knowball.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.query.Procedure;
import org.springframework.transaction.annotation.Transactional;

import br.com.fiap.knowball.model.Team;

public interface TeamRepository extends JpaRepository<Team, Long>{

    Optional<Team> findByName(String name);

    @Transactional
    @Modifying
    @Procedure(procedureName = "prc_insert_equipe")
    void insertTeam(String p_nome, String p_cidade, String p_estado);

    @Transactional
    @Modifying
    @Procedure(procedureName = "prc_update_equipe")
    void updateTeam(Long p_id_equipe, String p_nome, String p_cidade, String p_estado);

    @Transactional
    @Modifying
    @Procedure(procedureName = "prc_delete_equipe")
    void deleteTeam(Long p_id_equipe);
}
