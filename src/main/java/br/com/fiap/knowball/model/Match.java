package br.com.fiap.knowball.model;

import java.time.LocalDateTime;
import java.util.Set;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Match {
    
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull(message = "{match.championship.notnull}")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "championship_id", nullable = false)
    private Championship championship;

    @NotNull(message = "{match.date.notnull}")
    @Column(nullable = false)
    private LocalDateTime date;
    
    @NotNull(message = "{match.teams.notnull}")
    @Size(min = 2, message = "{match.teams.size}")
    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
        name = "match_team",
        joinColumns = @JoinColumn(name = "match_id"),
        inverseJoinColumns = @JoinColumn(name = "team_id")
    ) // define explicitamente a tabela de junção que será criada no banco para armazenar o relacionamento
    private Set<Team> teams; // significa que cada objeto da classe Match pode estar associdado a vários objetos da classe Team
    //formando uma coleção de times participantes daquela partida.

}
