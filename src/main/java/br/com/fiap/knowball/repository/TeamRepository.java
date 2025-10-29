package br.com.fiap.knowball.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.query.Procedure;
import org.springframework.data.repository.query.Param;

import br.com.fiap.knowball.model.Team;

public interface TeamRepository extends JpaRepository<Team, Long>{
    Optional<Team> findByName(String name);

    @Procedure(procedureName = "prc_insert_equipe")
    void insertTeamProcedure(
        @Param("p_nome") String name,
        @Param("p_cidade") String city,
        @Param("p_estado") String state
    );

    @Procedure(procedureName = "prc_update_equipe")
    void updateTeamProcedure(
        @Param("p_id_team") Long id,
        @Param("p_nome") String nome,
        @Param("p_cidade") String cidade,
        @Param("p_estado") String estado
    );

    @Procedure(procedureName = "prc_delete_equipe")
    void deleteTeamProcedure(@Param("p_id_team") Long id);
}
