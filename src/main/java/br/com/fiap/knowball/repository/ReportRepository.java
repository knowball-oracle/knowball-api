package br.com.fiap.knowball.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import br.com.fiap.knowball.model.Report;
import br.com.fiap.knowball.model.ReportStatusType;

import java.util.List;
import java.util.Optional;


public interface ReportRepository extends JpaRepository<Report, Long>{
    
    @Query("SELECT r FROM Report r WHERE r.status = :status")
    List<Report> findByStatus(@Param("status") ReportStatusType status);

    Optional<Report> findbyProtocol(String protocol);
}
