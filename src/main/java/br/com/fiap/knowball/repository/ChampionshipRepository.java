package br.com.fiap.knowball.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.query.Procedure;
import org.springframework.data.repository.query.Param;

import br.com.fiap.knowball.model.Championship;

public interface ChampionshipRepository extends JpaRepository<Championship, Long>{

    @Procedure(procedureName = "pcd_insert_campeonato")
    void insertChampionshipProcedure (
        @Param("p_nome") String name,
        @Param("p_categoria") String category,
        @Param("p_ano") Integer year
    );

    @Procedure(procedureName = "prc_update_campeonato")
    void updateChampionshipProcedure(
        @Param("p_id_campeonato") Long id,
        @Param("p_nome") String name,
        @Param("p_categoria") String category,
        @Param("p_ano") Integer year
    );

    @Procedure(procedureName = "prc_delete_campeonato")
    void deleteChampionshipProcedure(@Param("p_id_campeonato") Long id);
}
