package br.com.fiap.knowball.service;

import java.util.List;

import org.hibernate.validator.internal.util.stereotypes.Lazy;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.fiap.knowball.model.Report;
import br.com.fiap.knowball.model.ReportStatusType;
import br.com.fiap.knowball.repository.GameRepository;
import br.com.fiap.knowball.repository.RefereeRepository;
import br.com.fiap.knowball.repository.RefereeingRepository;
import br.com.fiap.knowball.repository.ReportRepository;
import jakarta.persistence.EntityNotFoundException;

@Service
public class ReportService {

    @Autowired
    private final ReportRepository reportRepository;
    private final GameRepository matchRepository;
    private final RefereeRepository refereeRepository;
    private final RefereeingRepository refereeingRepository;

    @Autowired
    @Lazy
    private final RefereeService refereeService;

    public ReportService(ReportRepository reportRepository,
            GameRepository matchRepository,
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
        Long matchId = report.getGame().getId();
        Long refereeId = report.getReferee().getId();

        matchRepository.findById(matchId)
                .orElseThrow(() -> new EntityNotFoundException("Partida não encontrada"));

        refereeRepository.findById(refereeId)
                .orElseThrow(() -> new EntityNotFoundException("Arbitro não encontrado"));

        boolean isRefereeActing = refereeingRepository.findByGameId(matchId).stream()
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
