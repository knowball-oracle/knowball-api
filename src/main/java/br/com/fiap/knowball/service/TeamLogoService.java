package br.com.fiap.knowball.service;

import br.com.fiap.knowball.dto.TheSportsDbSearchResponse;
import br.com.fiap.knowball.dto.TheSportsDbTeamDTO;
import br.com.fiap.knowball.model.Team;
import br.com.fiap.knowball.repository.TeamRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.List;
import java.util.Optional;

@Service
@Slf4j
public class TeamLogoService {

    @Value("${football.api.base-url}")
    private String baseUrl;

    @Value("${football.api.key}")
    private String apiKey;

    private final RestTemplate restTemplate = new RestTemplate();
    private final TeamRepository teamRepository;

    public TeamLogoService(TeamRepository teamRepository) {
        this.teamRepository = teamRepository;
    }

    public boolean syncLogo(Team team) {
        Optional<String> badgeUrl = fetchBadgeUrl(team.getName());

        if (badgeUrl.isEmpty()) {
            log.warn("Nenhum escudo encontrado na TheSportsDB para o time '{}'", team.getName());
            return false;
        }

        team.setLogoUrl(badgeUrl.get());
        teamRepository.save(team);
        log.info("Escudo sincronizado para '{}': {}", team.getName(), badgeUrl.get());
        return true;
    }

    public TeamLogoSyncResult syncAllMissingLogos() {
        List<Team> teamsWithoutLogo = teamRepository.findAllByLogoUrlIsNull();

        int updated = 0;
        int notFound = 0;

        for (Team team : teamsWithoutLogo) {
            try {
                boolean success = syncLogo(team);
                if (success) {
                    updated++;
                } else {
                    notFound++;
                }

                Thread.sleep(250);
            } catch (HttpClientErrorException.TooManyRequests e) {
                log.warn("Rate limit da TheSportsDB atingido. Interrompendo sincronização.");
                break;
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                break;
            } catch (Exception e) {
                log.error("Erro ao sincronizar escudo do time '{}': {}", team.getName(), e.getMessage());
                notFound++;
            }
        }

        return new TeamLogoSyncResult(teamsWithoutLogo.size(), updated, notFound);
    }

    private Optional<String> fetchBadgeUrl(String teamName) {
        String url = UriComponentsBuilder
                .fromHttpUrl(baseUrl)
                .pathSegment(apiKey, "searchteams.php")
                .queryParam("t", teamName)
                .encode()
                .toUriString();

        try {
            TheSportsDbSearchResponse response =
                    restTemplate.getForObject(url, TheSportsDbSearchResponse.class);

            if (response == null || response.teams() == null || response.teams().isEmpty()) {
                log.warn("TheSportsDB não encontrou resultados para '{}'", teamName);
                return Optional.empty();
            }

            TheSportsDbTeamDTO bestMatch = response.teams().stream()
                    .filter(team -> "Brazil".equalsIgnoreCase(team.strCountry()))
                    .findFirst()
                    .orElse(response.teams().get(0));

            log.info(
                    "TheSportsDB encontrou '{}' para '{}'. País: '{}'. Badge: '{}'",
                    bestMatch.strTeam(),
                    teamName,
                    bestMatch.strCountry(),
                    bestMatch.strBadge()
            );

            return Optional.ofNullable(bestMatch.strBadge())
                    .filter(badge -> !badge.isBlank());

        } catch (HttpClientErrorException.TooManyRequests e) {
            throw e;
        } catch (Exception e) {
            log.error(
                    "Falha ao consultar TheSportsDB para '{}': {}",
                    teamName,
                    e.getMessage(),
                    e
            );
            return Optional.empty();
        }
    }

    public record TeamLogoSyncResult(int total, int updated, int notFound) {}
}
