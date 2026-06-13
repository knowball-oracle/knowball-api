package br.com.fiap.knowball.service;

import java.time.LocalDate;
import java.util.List;

import br.com.fiap.knowball.model.*;

import br.com.fiap.knowball.repository.*;
import org.springframework.beans.BeanUtils;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import jakarta.persistence.EntityNotFoundException;

@Service
public class ReportService {

    private final ReportRepository reportRepository;
    private final GameRepository matchRepository;
    private final RefereeRepository refereeRepository;
    private final RefereeingRepository refereeingRepository;
    private final UserRepository userRepository;
    private final RefereeService refereeService;

    public ReportService(ReportRepository reportRepository,
                         GameRepository matchRepository,
                         RefereeRepository refereeRepository,
                         RefereeingRepository refereeingRepository, UserRepository userRepository,
                         @Lazy RefereeService refereeService) {
        this.reportRepository = reportRepository;
        this.matchRepository = matchRepository;
        this.refereeRepository = refereeRepository;
        this.refereeingRepository = refereeingRepository;
        this.userRepository = userRepository;
        this.refereeService = refereeService;
    }

    public List<Report> findAll() {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new EntityNotFoundException("Usuário não encontrado"));

        if (user.getRole() == UserRole.ROLE_ADMIN) {
            return reportRepository.findAll();
        }
        return reportRepository.findByUserId(user.getId());
    }

    public Report findById(Long id) {
        return reportRepository
                .findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Denúncia não encontrado com id " + id));
    }

    public List<Report> findByStatus(ReportStatusType status) {
        return reportRepository.findByStatus(status);
    }

    private String generateProtocol() {
        int year = LocalDate.now().getYear();
        String prefix = "KB-" + year + "-";

        int nextNumber = reportRepository
                .findLastProtocolo(prefix)
                .map(lastProtocol -> {
                    String[] parts = lastProtocol.split("-");
                    return Integer.parseInt(parts[2]) + 1;
                })
                .orElse(1);

        return String.format("%s%03d", prefix, nextNumber);
    }

    public Report save(Report report) {
        Long matchId = report.getGame().getId();
        Long refereeId = report.getReferee().getId();

        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new EntityNotFoundException("Usuário não encontrado"));
        report.setUser(user);

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

        if (report.getStatus() == null) {
            report.setStatus(ReportStatusType.UNDER_REVIEW);
        }

        report.setProtocol(generateProtocol());

        Report saved = reportRepository.save(report);
        refereeService.suspendReferee(refereeId);
        return saved;
    }

    public Report update(Long id, Report report) {
        Report existing = findById(id);
        BeanUtils.copyProperties(report, existing, "id");
        return save(existing);
    }

    public Report updateStatus(Long id, ReportStatusType status, AnalysisResultType analysisResult) {
        Report report = findById(id);
        report.setStatus(status);
        report.setAnalysisResult(analysisResult);
        return reportRepository.save(report);
    }

    public void deleteByIdWithPermissionCheck(Long id) {
        Report report = reportRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Denúncia não encontrada com id " + id));

        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        User principal = userRepository.findByEmail(email)
                .orElseThrow(() -> new EntityNotFoundException("Usuário autenticado não encontrado"));

        boolean isAdmin = principal.getRole() == UserRole.ROLE_ADMIN;

        boolean isOwner = report.getUser() != null
                && report.getUser().getId() != null
                && report.getUser().getId().equals(principal.getId());

        if (!isAdmin && !isOwner) {
            throw new AccessDeniedException("Usuário não pode deletar esta denúncia");
        }

        reportRepository.delete(report);
    }
}
