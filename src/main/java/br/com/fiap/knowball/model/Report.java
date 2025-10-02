package br.com.fiap.knowball.model;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.validation.constraints.NotBlank;
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
public class Report {
    
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "{report.protocol.notblank}")
    @Column(nullable = false, unique = true) // define características da coluna no banco de dados
    private String protocol;

    @NotBlank(message = "{report.description.notblank}")
    @Size(min = 10, max = 1000, message = "{report.description.size}")
    @Column(nullable = false, unique = true)
    private String description;

    @NotNull(message = "{report.statusType.notnull}")
    @Enumerated(EnumType.STRING)
    @Column(nullable = false, unique = true)
    private ReportStatusType statusType;

    @NotBlank(message = "{report.feeling.notblank}")
    @Column(nullable = false)
    private String feeling;

    @NotNull(message = "{report.registrationDate.notnull}")
    @Column(nullable = false)
    private LocalDateTime registrationDate;

    @NotNull(message = "{report.match.notnull}")
    @ManyToOne(fetch = FetchType.LAZY) //define relacionamento de muitos-para-um entre entidades
    @JoinColumn(name = "match_id", nullable = false) //define a coluna da tabela que será a chave estrangeira para o relacionamento
    private Match match;
}
