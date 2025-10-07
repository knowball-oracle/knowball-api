package br.com.fiap.knowball.service;

import java.util.List;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.fiap.knowball.model.Report;
import br.com.fiap.knowball.model.ReportStatusType;
import br.com.fiap.knowball.repository.MatchRepository;
import br.com.fiap.knowball.repository.RefereeRepository;
import br.com.fiap.knowball.repository.RefereeingRepository;
import br.com.fiap.knowball.repository.ReportRepository;
import jakarta.persistence.EntityNotFoundException;

@Service
public class ReportService {

    @Autowired
    private final ReportRepository reportRepository;
    private final MatchRepository matchRepository;
    private final RefereeRepository refereeRepository;
    private final RefereeingRepository refereeingRepository;
    private final RefereeService refereeService;

    public ReportService(ReportRepository reportRepository,
            MatchRepository matchRepository,
            RefereeRepository refereeRepository,
            RefereeingRepository refereeingRepository,
            RefereeService refereeService) {
        this.reportRepository = reportRepository;
        this.matchRepository = matchRepository;
        this.refereeRepository = refereeRepository;
        this.refereeingRepository = refereeingRepository;
        this.refereeService = refereeService;
    }

    public List<Report> findAll() {
        return reportRepository.findAll();
    }

    public Report findById(Long id) {
        return reportRepository
                .findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Denúncia não encontrado com id " + id));
    }

    public List<Report> findByStatus(ReportStatusType status) {
        return reportRepository.findByStatus(status);
    }

    public Report save(Report report) {
        Long matchId = report.getMatch().getId();
        Long refereeId = report.getReferee().getId();

        matchRepository.findById(matchId)
                .orElseThrow(() -> new EntityNotFoundException("Partida não encontrada"));

        refereeRepository.findById(refereeId)
                .orElseThrow(() -> new EntityNotFoundException("Arbitro não encontrado"));

        boolean isRefereeActing = refereeingRepository.findByMatchId(matchId).stream()
                .anyMatch(r -> r.getReferee().getId().equals(refereeId));

        if (!isRefereeActing) {
            throw new IllegalArgumentException(
                    "Não é possível denúnciar um árbitro que não estava apitando esta partida");
        }

        Report saved = reportRepository.save(report);

        refereeService.suspendReferee(refereeId);

        return saved;
    }

    public Report update(Long id, Report report) {
        Report existing = findById(id);
        BeanUtils.copyProperties(report, existing, "id");
        return save(existing);
    }

    public void destroy(Long id) {
        reportRepository.deleteById(id);
    }
}
