package br.com.fiap.knowball.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.fiap.knowball.dto.LoginRequest;
import br.com.fiap.knowball.dto.LoginResponse;
import br.com.fiap.knowball.model.User;
import br.com.fiap.knowball.service.AuthService;
import br.com.fiap.knowball.service.TokenService;
import br.com.fiap.knowball.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Tag(name = "Auth", description = "Autenticação e registro público")
@RestController
@RequestMapping("auth")
@RequiredArgsConstructor
@Slf4j
public class AuthController {

    private final AuthService authService;
    private final UserService userService;
    private final TokenService tokenService;

    public record RegisterRequest (
        @NotBlank String name,
        @NotBlank String email,
        @NotBlank String password
    ) {}

    @Operation(summary = "Login", description = "Autentica o usuário e retorna token JWT")
    @ApiResponse(responseCode = "200", description = "Token gerado com sucesso")
    @ApiResponse(responseCode = "401", description = "Credenciais inválidas")
    @PostMapping("/login")
    public ResponseEntity<LoginResponse> register(@Valid @RequestBody LoginRequest request) {
        log.info("POST /auth/login : {}", request.email());

        authService.authenticate(request.email(), request.password());

        User user = (User) authService.loadUserByUsername(request.email());
        String token = tokenService.generateToken(user);

        return ResponseEntity.ok(new LoginResponse(token, user.getEmail(), user.getName(), user.getRole().name()));
    }

    public ResponseEntity<LoginResponse> register(@Valid @RequestBody RegisterRequest request) {
        log.info("POST /auth/register : {}", request.email());

        User newUser = User.builder()
            .name(request.name())
            .email(request.email())
            .password(request.password())
            .build();

        User saved = userService.createUser(newUser);
        String token = tokenService.generateToken(saved);

        return ResponseEntity.status(201)
            .body(new LoginResponse(token, saved.getEmail(), saved.getName(), saved.getRole().name()));
    }
    
}
