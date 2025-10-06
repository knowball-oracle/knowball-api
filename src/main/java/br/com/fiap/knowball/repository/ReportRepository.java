package br.com.fiap.knowball.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import br.com.fiap.knowball.model.Report;
import br.com.fiap.knowball.model.ReportStatusType;

import java.util.List;
import java.util.Optional;


public interface ReportRepository extends JpaRepository<Report, Long>{
    Optional<Report> findbyProtocol(String protocol);
    List<Report> findByStatus(ReportStatusType status);
}
