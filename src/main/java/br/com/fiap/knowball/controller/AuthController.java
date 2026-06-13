package br.com.fiap.knowball.controller;

import br.com.fiap.knowball.dto.LoginRequest;
import br.com.fiap.knowball.dto.LoginResponse;
import br.com.fiap.knowball.dto.RegisterPendingResponse;
import br.com.fiap.knowball.dto.VerifyEmailRequest;
import br.com.fiap.knowball.model.User;
import br.com.fiap.knowball.service.AuthService;
import br.com.fiap.knowball.service.EmailVerificationService;
import br.com.fiap.knowball.service.TokenService;
import br.com.fiap.knowball.repository.UserRepository;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@Tag(name = "Auth", description = "Autenticação e registro público")
@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
@Slf4j
public class AuthController {

    private final AuthService authService;
    private final TokenService tokenService;
    private final AuthenticationManager authenticationManager;
    private final EmailVerificationService emailVerificationService;
    private final UserRepository userRepository;

    public record RegisterRequest(
            @NotBlank String name,
            @NotBlank String email,
            @NotBlank String password
    ) {}

    @Operation(summary = "Login", description = "Autentica o usuário e retorna token JWT")
    @ApiResponse(responseCode = "200", description = "Token gerado com sucesso")
    @ApiResponse(responseCode = "401", description = "Credenciais inválidas")
    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@Valid @RequestBody LoginRequest request) {
        log.info("POST /auth/login : {}", request.email());

        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.email(), request.password())
        );

        User user = (User) authService.loadUserByUsername(request.email());
        String token = tokenService.generateToken(user);

        return ResponseEntity.ok(new LoginResponse(
                token, user.getEmail(), user.getName(),
                user.getRole().name(), user.getProfilePicture()));
    }

    @Operation(summary = "Registrar", description = "Registra um novo usuário e envia código de verificação por e-mail")
    @ApiResponse(responseCode = "201", description = "Usuário registrado, aguardando verificação de e-mail")
    @ApiResponse(responseCode = "409", description = "Email já cadastrado")
    @PostMapping("/register")
    public ResponseEntity<RegisterPendingResponse> register(@Valid @RequestBody RegisterRequest request) {
        log.info("POST /auth/register : {}", request.email());

        authService.register(request.name(), request.email(), request.password());

        try {
            emailVerificationService.sendVerificationCode(request.email());
        } catch (RuntimeException e) {
            log.error("Falha ao enviar e-mail de verificação para {}: {}", request.email(), e.getMessage());
        }

        return ResponseEntity.status(201)
                .body(new RegisterPendingResponse("EMAIL_VERIFICATION_PENDING", request.email()));
    }

    @Operation(summary = "Verificar e-mail", description = "Valida o código enviado por e-mail e retorna o JWT")
    @ApiResponse(responseCode = "200", description = "E-mail verificado, token gerado")
    @ApiResponse(responseCode = "400", description = "Código inválido ou expirado")
    @PostMapping("/verify-email")
    public ResponseEntity<?> verifyEmail(@Valid @RequestBody VerifyEmailRequest request) {
        log.info("POST /auth/verify-email : {}", request.email());

        boolean valid = emailVerificationService.verifyCode(request.email(), request.code());
        if (!valid) {
            return ResponseEntity.badRequest()
                    .body(Map.of("message", "Código inválido ou expirado."));
        }

        User user = (User) authService.loadUserByUsername(request.email());
        String token = tokenService.generateToken(user);

        return ResponseEntity.ok(new LoginResponse(
                token, user.getEmail(), user.getName(),
                user.getRole().name(), user.getProfilePicture()));
    }

    @Operation(summary = "Reenviar código", description = "Reenvia o código de verificação para o e-mail informado")
    @ApiResponse(responseCode = "200", description = "Código reenviado com sucesso")
    @PostMapping("/resend-code")
    public ResponseEntity<Map<String, String>> resendCode(@RequestBody Map<String, String> body) {
        String email = body.get("email");
        log.info("POST /auth/resend-code : {}", email);
        emailVerificationService.sendVerificationCode(email);
        return ResponseEntity.ok(Map.of("message", "Código reenviado com sucesso."));
    }
}