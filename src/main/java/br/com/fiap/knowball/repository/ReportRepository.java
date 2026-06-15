package br.com.fiap.knowball.repository;

import br.com.fiap.knowball.dto.ReportByChampionshipDTO;
import br.com.fiap.knowball.dto.ReportByRefereeDTO;
import br.com.fiap.knowball.dto.ReportStatusCountDTO;
import br.com.fiap.knowball.model.Report;
import br.com.fiap.knowball.model.ReportStatusType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
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
    long countByDateRange(@Param("startDate") LocalDate startDate,
                          @Param("endDate") LocalDate endDate);

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
}
