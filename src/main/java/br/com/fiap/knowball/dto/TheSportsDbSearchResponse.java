package br.com.fiap.knowball.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public record TheSportsDbSearchResponse(
        List<TheSportsDbTeamDTO> teams
) {}
