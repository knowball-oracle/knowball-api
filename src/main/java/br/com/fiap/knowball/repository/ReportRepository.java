package br.com.fiap.knowball.repository;

import br.com.fiap.knowball.dto.*;
import br.com.fiap.knowball.model.Report;
import br.com.fiap.knowball.model.ReportStatusType;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

public interface ReportRepository extends JpaRepository<Report, Long>{
    
    @Query("SELECT r FROM Report r WHERE r.status = :status")
    List<Report> findByStatus(@Param("status") ReportStatusType status);

    Optional<Report> findByProtocol(String protocol);

    void deleteByGameId(Long gameId);

    List<Report> findByUserId(Long userId);

    @Query("SELECT r.protocol FROM Report r WHERE r.protocol LIKE :prefix% ORDER BY r.protocol DESC LIMIT 1")
    Optional<String> findLastProtocolo(@Param("prefix") String prefix);

    Optional<Report> findTopByProtocolStartingWithOrderByProtocolDesc(String prefix);

    @Query("SELECT new br.com.fiap.knowball.dto.ReportStatusCountDTO(r.status, COUNT(r)) " +
            "FROM Report r GROUP BY r.status")
    List<ReportStatusCountDTO> countByStatus();

    @Query("SELECT new br.com.fiap.knowball.dto.ReportByChampionshipDTO(" +
            "r.game.championship.name, CAST(r.game.championship.category AS string), COUNT(r)) " +
            "FROM Report r GROUP BY r.game.championship.name, r.game.championship.category")
    List<ReportByChampionshipDTO> countByChampionship();

    @Query("""
       SELECT COUNT(r)
       FROM Report r
       WHERE r.date >= :startDate
         AND r.date < :endDate
       """)
    long countByDateRange(@Param("startDate") Instant startDate,
                          @Param("endDate") Instant endDate);

    @Query("""
        SELECT new br.com.fiap.knowball.dto.ReportByRefereeDTO(
            r.referee.id,
            r.referee.name,
            COUNT(r)
        )
        FROM Report r
        GROUP BY r.referee.id, r.referee.name
        ORDER BY COUNT(r) DESC
        """)
    List<ReportByRefereeDTO> countByReferee();

    @Query("""
    SELECT new br.com.fiap.knowball.dto.ReportByResultDTO(r.analysisResult, COUNT(r))
    FROM Report r
    WHERE r.analysisResult IS NOT NULL
    GROUP BY r.analysisResult
    """)
    List<ReportByResultDTO> countByAnalysisResult();

    @Query("""
    SELECT new br.com.fiap.knowball.dto.ReportByCategoryDTO(
        CAST(r.game.championship.category AS string), COUNT(r))
    FROM Report r
    GROUP BY r.game.championship.category
    """)
    List<ReportByCategoryDTO> countByCategory();

    @Query("SELECT r.date FROM Report r WHERE r.date >= :startDate")
    List<Instant> findDatesFrom(@Param("startDate") Instant startDate);

    @Query("""
    SELECT new br.com.fiap.knowball.dto.ReportRecentDTO(
        r.id, r.protocol, r.status, r.analysisResult, r.date,
        r.referee.name, r.game.championship.name)
    FROM Report r
    ORDER BY r.date DESC
    """)
    List<ReportRecentDTO> findRecent(Pageable pageable);
}
