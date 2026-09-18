package br.com.fiap.knowball.config;

import br.com.fiap.knowball.model.User;
import br.com.fiap.knowball.repository.UserRepository;
import br.com.fiap.knowball.service.TokenService;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.IOException;

@Component
public class OAuth2LoginSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

    private final UserRepository userRepository;
    private final TokenService tokenService;
    private final String frontendUrl;

    private static final String FRONTEND_CALLBACK_PATH = "/auth/callback";

    public OAuth2LoginSuccessHandler(UserRepository userRepository, TokenService tokenService, @Value("${app.frontend-url}") String frontendUrl) {
        this.userRepository = userRepository;
        this.tokenService = tokenService;
        this.frontendUrl = frontendUrl;
    }

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {

        OAuth2User oAuth2User = (OAuth2User) authentication.getPrincipal();
        String email = oAuth2User.getAttribute("email");

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalStateException(
                        "Usuário Google autenticado mas não encontrado no banco: " + email));

        String token = tokenService.generateToken(user);

        String targetUrl = UriComponentsBuilder
                .fromUriString(frontendUrl + FRONTEND_CALLBACK_PATH)
                .queryParam("token", token)
                .build()
                .toUriString();

        getRedirectStrategy().sendRedirect(request, response, targetUrl);
    }
}