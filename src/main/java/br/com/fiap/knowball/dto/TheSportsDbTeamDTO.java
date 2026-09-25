package br.com.fiap.knowball.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public record TheSportsDbTeamDTO(
        String idTeam,
        String strTeam,
        String strBadge,
        String strCountry,
        String strLeague
) {}
