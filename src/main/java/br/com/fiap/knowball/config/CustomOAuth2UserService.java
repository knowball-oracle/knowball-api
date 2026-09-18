package br.com.fiap.knowball.config;

import br.com.fiap.knowball.model.AuthProvider;
import br.com.fiap.knowball.model.User;
import br.com.fiap.knowball.model.UserRole;
import br.com.fiap.knowball.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CustomOAuth2UserService extends DefaultOAuth2UserService {

    private final UserRepository userRepository;

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        OAuth2User oAuth2User = super.loadUser(userRequest);

        String email = oAuth2User.getAttribute("email");
        String name = oAuth2User.getAttribute("name");
        String googleId = oAuth2User.getAttribute("sub");
        String picture = oAuth2User.getAttribute("picture");

        if (email == null) {
            throw new OAuth2AuthenticationException("Não foi possível obter o e-mail da conta Google.");
        }

        User user = userRepository.findByEmail(email)
                .map(existing -> linkGoogleAccount(existing, googleId))
                .orElseGet(() -> createNewGoogleUser(email, name, googleId, picture));

        return oAuth2User;
    }

    private User linkGoogleAccount(User existing, String googleId) {
        if (existing.getGoogleId() == null) {
            existing.setGoogleId(googleId);
            userRepository.save(existing);
        }
        return existing;
    }

    private User createNewGoogleUser(String email, String name, String googleId, String picture) {
        User newUser = User.builder()
                .name(name != null ? name : email)
                .email(email)
                .password(null)
                .role(UserRole.ROLE_USER)
                .authProvider(AuthProvider.GOOGLE)
                .googleId(googleId)
                .profilePicture(picture)
                .build();

        return userRepository.save(newUser);
    }
}
