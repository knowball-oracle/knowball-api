package br.com.fiap.knowball.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import br.com.fiap.knowball.model.Team;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

public interface TeamRepository extends JpaRepository<Team, Long>{

    Optional<Team> findByName(String name);

    List<Team> findAllByLogoUrlIsNull();

    @Modifying(clearAutomatically = true, flushAutomatically = true)
    @Transactional
    @Query("""
        UPDATE Team t
           SET t.logoUrl = :logoUrl
         WHERE t.id = :teamId
    """)
    int updateLogoUrl(
            @Param("teamId") Long teamId,
            @Param("logoUrl") String logoUrl
    );

}
