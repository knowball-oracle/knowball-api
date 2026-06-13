package br.com.fiap.knowball.service;

import br.com.fiap.knowball.dto.RegisterPendingResponse;
import br.com.fiap.knowball.exception.EmailAlreadyExistsException;
import br.com.fiap.knowball.model.User;
import br.com.fiap.knowball.model.UserRole;
import br.com.fiap.knowball.repository.UserRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService implements UserDetailsService{

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder passwordEncoder;
    private final EmailVerificationService emailVerificationService;

    public AuthService(UserRepository userRepository, BCryptPasswordEncoder passwordEncoder, EmailVerificationService emailVerificationService) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.emailVerificationService = emailVerificationService;
    }
    
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userRepository.findByEmail(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with email: " + username));
    }

    public RegisterPendingResponse register(String name, String email, String rawPassword) {
        if (userRepository.existsByEmail(email))
            throw new EmailAlreadyExistsException("Email já cadastrado: " + email);

        User user = User.builder()
                .name(name)
                .email(email)
                .password(passwordEncoder.encode(rawPassword))
                .role(UserRole.ROLE_USER)
                .emailVerified(false)
                .build();
        userRepository.save(user);

        emailVerificationService.sendVerificationCode(email);

        return new RegisterPendingResponse("EMAIL_VERIFICATION_PENDING", email);
    }
}
