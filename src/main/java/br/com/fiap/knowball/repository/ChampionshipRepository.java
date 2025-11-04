package br.com.fiap.knowball.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.query.Procedure;
import org.springframework.transaction.annotation.Transactional;

import br.com.fiap.knowball.model.Championship;
import br.com.fiap.knowball.model.ChampionshipCategoryType;

public interface ChampionshipRepository extends JpaRepository<Championship, Long>{

    Optional<Championship> findByNameAndCategoryAndYear(String name, ChampionshipCategoryType category, Integer year);

    @Transactional
    @Modifying
    @Procedure(procedureName = "prc_insert_campeonato")
    void insertChampionship(String p_nome, String p_categoria, Integer p_ano);

    @Transactional
    @Modifying
    @Procedure(procedureName = "prc_update_campeonato")
    void updateChampionship(Long p_id_campeonato, String p_nome, String p_categoria, Integer p_ano);

    @Transactional
    @Modifying
    @Procedure(procedureName = "prc_delete_campeonato")
    void deleteChampionship(Long p_id_campeonato);
}
