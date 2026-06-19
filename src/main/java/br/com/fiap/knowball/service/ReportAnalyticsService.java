package br.com.fiap.knowball.service;

import br.com.fiap.knowball.dto.*;
import br.com.fiap.knowball.model.ReportStatusType;
import br.com.fiap.knowball.repository.ReportRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.YearMonth;
import java.time.ZoneOffset;
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
        YearMonth currentMonth = YearMonth.now();

        Instant startOfMonth    = currentMonth.atDay(1)
                .atStartOfDay(ZoneOffset.UTC).toInstant();
        Instant startOfNextMonth = currentMonth.plusMonths(1).atDay(1)
                .atStartOfDay(ZoneOffset.UTC).toInstant();

        long total = reportRepository.count();
        List<ReportStatusCountDTO> byStatus      = reportRepository.countByStatus();
        List<ReportByChampionshipDTO> byChampionship = reportRepository.countByChampionship();
        long thisMonth = reportRepository.countByDateRange(startOfMonth, startOfNextMonth);

        long resolved = byStatus.stream()
                .filter(s -> s.status() == ReportStatusType.RESOLVED)
                .mapToLong(ReportStatusCountDTO::count)
                .sum();

        long pending = byStatus.stream()
                .filter(s -> s.status() == ReportStatusType.NEW
                        || s.status() == ReportStatusType.UNDER_REVIEW)
                .mapToLong(ReportStatusCountDTO::count)
                .sum();

        return new ReportAnalyticsSummaryDTO(total, byStatus, byChampionship, thisMonth, resolved, pending);
    }

    public ReportKpiDTO getKpis() {
        ReportAnalyticsSummaryDTO summary = getSummary();
        return new ReportKpiDTO(
                summary.totalReports(),
                summary.reportsThisMonth(),
                summary.resolvedCount(),
                summary.pendingCount()
        );
    }

    public List<ReportByRefereeDTO> getCountByReferee() {
        return reportRepository.countByReferee();
    }
}