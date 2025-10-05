package br.com.fiap.knowball.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Team {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "{team.name.notblank}")
    @Column(nullable = false, length = 100, unique = true)
    private String name;

    @NotBlank(message = "{team.city.notblank}")
    @Column(nullable = false, length = 100)
    private String city;

    @NotBlank(message = "{team.state.notblank}")
    @Column(nullable = false, length = 2)
    private String state;
}
