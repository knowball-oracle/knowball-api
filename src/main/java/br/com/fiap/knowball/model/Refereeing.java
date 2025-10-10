package br.com.fiap.knowball.model;

import jakarta.persistence.Column;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.MapsId;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Refereeing {
    
    @EmbeddedId
    private RefereeingId id;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private RefereeingRoleType role;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("matchId")
    @JoinColumn(name = "match_id")
    private Game game;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("refereeId")
    @JoinColumn(name = "referee_id")
    private Referee referee;
}
