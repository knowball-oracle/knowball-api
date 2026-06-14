package br.com.fiap.knowball.service;

import br.com.fiap.knowball.dto.ReportAnalyticsSummaryDTO;
import br.com.fiap.knowball.dto.ReportByChampionshipDTO;
import br.com.fiap.knowball.dto.ReportStatusCountDTO;
import br.com.fiap.knowball.model.ReportStatusType;
import br.com.fiap.knowball.repository.ReportRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ReportAnalyticsService {

    private final ReportRepository reportRepository;

    public List<ReportStatusCountDTO> getCountByStatus() {
        return reportRepository.countByStatus();
    }

    public List<ReportByChampionshipDTO> getCountByChampionship() {
        return reportRepository.countByChampionship();
    }

    public ReportAnalyticsSummaryDTO getSummary() {
        LocalDate now = LocalDate.now();

        long total = reportRepository.count();
        List<ReportStatusCountDTO> byStatus = reportRepository.countByStatus();
        List<ReportByChampionshipDTO> byChampionship = reportRepository.countByChampionship();
        long thisMonth = reportRepository.countByYearAndMonth(now.getYear(), now.getMonthValue());

        long resolved = byStatus.stream()
                .filter(s -> s.status() == ReportStatusType.RESOLVED)
                .mapToLong(ReportStatusCountDTO::count)
                .sum();

        long pending = byStatus.stream()
                .filter(s -> s.status() == ReportStatusType.NEW || s.status() == ReportStatusType.UNDER_REVIEW)
                .mapToLong(ReportStatusCountDTO::count)
                .sum();

        return new ReportAnalyticsSummaryDTO(total, byStatus, byChampionship, thisMonth, resolved, pending);
    }
}