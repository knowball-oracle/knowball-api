package br.com.fiap.knowball.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public record TheSportsDbTeamDTO(
        String idTeam,
        String strTeam,
        String strTeamBadge,
        String strCountry,
        String strLeague
) {}
