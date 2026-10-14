package br.com.fiap.knowball.service;

import br.com.fiap.knowball.dto.*;
import br.com.fiap.knowball.model.AnalysisResultType;
import br.com.fiap.knowball.model.ChampionshipCategoryType;
import br.com.fiap.knowball.model.ReportStatusType;
import br.com.fiap.knowball.repository.ReportRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.YearMonth;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.util.EnumMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class ReportAnalyticsService {

    private static final ZoneId ZONE = ZoneId.of("America/Sao_Paulo");
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

    public List<ReportByResultDTO> getCountByResult() {
        Map<AnalysisResultType, Long> counts = new EnumMap<>(AnalysisResultType.class);

        for (AnalysisResultType type : AnalysisResultType.values()) {
            counts.put(type, 0L);
        }

        reportRepository.countByAnalysisResult()
                .forEach(row -> counts.put(row.result(), row.count()));

        return counts.entrySet().stream()
                .map(entry -> new ReportByResultDTO(entry.getKey(), entry.getValue()))
                .toList();
    }

    public List<ReportByCategoryDTO> getCountByCategory() {
        Map<String, Long> counts = new LinkedHashMap<>();

        for (ChampionshipCategoryType category : ChampionshipCategoryType.values()) {
            counts.put(category.name(), 0L);
        }

        reportRepository.countByCategory()
                .forEach(row -> counts.merge(row.category(), row.count(), Long::sum));

        return counts.entrySet().stream()
                .map(entry -> new ReportByCategoryDTO(entry.getKey(), entry.getValue()))
                .toList();
    }

    public List<ReportByMonthDTO> getCountByMonth(int months) {
        int window = Math.clamp(months, 1, 24);

        YearMonth current = YearMonth.now(ZONE);
        YearMonth first = current.minusMonths(window - 1L);
        Instant start = first.atDay(1).atStartOfDay(ZONE).toInstant();

        Map<YearMonth, Long> counts = new LinkedHashMap<>();
        for (int i = 0; i < window; i++) {
            counts.put(first.plusMonths(i), 0L);
        }

        reportRepository.findDatesFrom(start).forEach(date ->
                counts.computeIfPresent(YearMonth.from(date.atZone(ZONE)), (month, total) -> total + 1));

        return counts.entrySet().stream()
                .map(entry -> new ReportByMonthDTO(
                        entry.getKey().toString(),
                        String.format("%02d/%d", entry.getKey().getMonthValue(), entry.getKey().getYear()),
                        entry.getValue()))
                .toList();
    }

    public List<ReportRecentDTO> getRecent(int limit) {
        int size = Math.clamp(limit, 1, 50);
        return reportRepository.findRecent(PageRequest.of(0, size));
    }
}