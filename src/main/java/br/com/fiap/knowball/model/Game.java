package br.com.fiap.knowball.model;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "game")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Game {
    
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull(message = "{match.championship.notnull}")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "championship_id", nullable = false)
    private Championship championship;

    @NotNull(message = "{match.date.notnull}")
    @Column(name = "match_date", nullable = false)
    private LocalDateTime matchDate;
    
    @NotBlank(message = "{match.place.notblank}")
    @Column(nullable = false, length = 100)
    private String place;

    @NotNull(message = "{match.homeScore.notnull}")
    @Column(nullable = false)
    private Integer homeScore;

    @NotNull(message = "{match.awayScore.notnull}")
    @Column(nullable = false)
    private Integer awayScore;
}
