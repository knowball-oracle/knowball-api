package br.com.fiap.knowball.controller;

import br.com.fiap.knowball.dto.ConfirmVerificationCodeRequest;
import br.com.fiap.knowball.dto.LoginRequest;
import br.com.fiap.knowball.dto.LoginResponse;
import br.com.fiap.knowball.dto.SendVerificationCodeRequest;
import br.com.fiap.knowball.model.User;
import br.com.fiap.knowball.service.AuthService;
import br.com.fiap.knowball.service.EmailVerificationService;
import br.com.fiap.knowball.service.TokenService;
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
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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

    public record RegisterRequest(
            @NotBlank String name,
            @NotBlank String email,
            @NotBlank String password
    ) {}

    @Operation(summary = "Login")
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
                token, user.getId(), user.getEmail(), user.getName(),
                user.getRole().name(), user.getProfilePicture()));
    }

    @Operation(summary = "Registrar", description = "Registra um novo usuário e retorna o JWT diretamente")
    @ApiResponse(responseCode = "201", description = "Usuário registrado com sucesso")
    @ApiResponse(responseCode = "409", description = "Email já cadastrado")
    @PostMapping("/register")
    public ResponseEntity<LoginResponse> register(@Valid @RequestBody RegisterRequest request) {
        log.info("POST /auth/register : {}", request.email());

        if (!emailVerificationService.hasRecentVerifiedEmail(request.email())) {
            throw new IllegalArgumentException("E-mail não verificado ou verificação expirada. Solicite um novo código.");
        }

        authService.register(request.name(), request.email(), request.password());

        User user = (User) authService.loadUserByUsername(request.email());
        String token = tokenService.generateToken(user);

        return ResponseEntity.status(201).body(new LoginResponse(
                token, user.getId(), user.getEmail(), user.getName(),
                user.getRole().name(), user.getProfilePicture()));
    }

    @Operation(summary = "Enviar código de verificação de e-mail",
            description = "Gera um código de 6 dígitos e envia por e-mail via Brevo. Usado na Etapa 4 do cadastro.")
    @PostMapping("/verification/send")
    public ResponseEntity<Void> sendVerificationCode(@Valid @RequestBody SendVerificationCodeRequest request) {
        emailVerificationService.generateAndSendCode(request.email());
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "Confirmar código de verificação de e-mail",
            description = "Valida o código de 6 dígitos enviado por e-mail.")
    @PostMapping("/verification/confirm")
    public ResponseEntity<Void> confirmVerificationCode(@Valid @RequestBody ConfirmVerificationCodeRequest request) {
        emailVerificationService.verifyCode(request.email(), request.code());
        return ResponseEntity.ok().build();
    }
}