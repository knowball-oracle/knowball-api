package br.com.fiap.knowball.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;

@Entity
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Report {
    
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull(message = "{report.match.notnull}")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "game_id", nullable = false)
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
    private Game game;

    @NotNull(message = "{report.referee.notnull}")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "referee_id", nullable = false)
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
    private Referee referee;

    @Column(unique = true)
    private String protocol;

    @NotBlank(message = "{report.content.notblank}")
    @Lob
    @Column(nullable = false)
    private String content;

    @Column(name = "report_date", nullable = false)
    private Instant date;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private ReportStatusType status;

    @Enumerated(EnumType.STRING)
    @Column(length = 20)
    private AnalysisResultType analysisResult;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    @JsonIgnoreProperties({
            "hibernateLazyInitializer", "handler",
            "password", "profilePicture",
            "authorities", "username",
            "accountNonExpired", "accountNonLocked",
            "credentialsNonExpired", "enabled"
    })
    private User user;
}
