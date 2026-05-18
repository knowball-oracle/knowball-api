package br.com.fiap.knowball.service;

import java.util.List;

import br.com.fiap.knowball.model.AnalysisResultType;
import br.com.fiap.knowball.model.User;
import br.com.fiap.knowball.repository.*;
import org.hibernate.validator.internal.util.stereotypes.Lazy;
import org.springframework.beans.BeanUtils;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import br.com.fiap.knowball.model.Report;
import br.com.fiap.knowball.model.ReportStatusType;
import jakarta.persistence.EntityNotFoundException;

@Service
public class ReportService {

    private final ReportRepository reportRepository;
    private final GameRepository matchRepository;
    private final RefereeRepository refereeRepository;
    private final RefereeingRepository refereeingRepository;
    private final UserRepository userRepository;

    @Lazy
    private final RefereeService refereeService;

    public ReportService(ReportRepository reportRepository,
                         GameRepository matchRepository,
                         RefereeRepository refereeRepository,
                         RefereeingRepository refereeingRepository, UserRepository userRepository,
                         RefereeService refereeService) {
        this.reportRepository = reportRepository;
        this.matchRepository = matchRepository;
        this.refereeRepository = refereeRepository;
        this.refereeingRepository = refereeingRepository;
        this.userRepository = userRepository;
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

        String authenticatedUserEmail = SecurityContextHolder.getContext().getAuthentication().getName();

        User authenticatedUser = userRepository.findByEmail(authenticatedUserEmail)
                .orElseThrow(() -> new EntityNotFoundException("Usuário autenticado não encontrado"));

        report.setUser(authenticatedUser);

        Report saved = reportRepository.save(report);

        refereeService.suspendReferee(refereeId);

        return saved;
    }

    public Report update(Long id, Report report) {
        Report existing = findById(id);
        BeanUtils.copyProperties(report, existing, "id", "user");
        return save(existing);
    }

    public Report updateStatus(Long id, ReportStatusType status, AnalysisResultType analysisResult) {
        Report report = findById(id);
        report.setStatus(status);
        report.setAnalysisResult(analysisResult);
        return reportRepository.save(report);
    }

    public void deleteReport(Long id, String authenticatedUserEmail) {
        Report report = findById(id);

        User authenticatedUser = userRepository.findByEmail(authenticatedUserEmail)
                .orElseThrow(() -> new EntityNotFoundException("Usuário autenticado não encontrado"));

        boolean isAdmin = authenticatedUser.getRole().name().equals("ROLE_ADMIN");
        boolean isOwner = report.getUser() != null && report.getUser().getId().equals(authenticatedUser.getId());

        if (!isAdmin && !isOwner) {
            throw new AccessDeniedException("Você não tem permissão para excluir esta denúncia");
        }

        reportRepository.delete(report);
    }
}
