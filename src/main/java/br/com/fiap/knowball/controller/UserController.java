package br.com.fiap.knowball.controller;

import br.com.fiap.knowball.dto.UserResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import br.com.fiap.knowball.model.User;
import br.com.fiap.knowball.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Tag(name = "Users", description = "Gestão de usuários - requer ROLE_ADMIN")
@SecurityRequirement(name = "bearerAuth")
@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
@Slf4j
@PreAuthorize("hasRole('ADMIN')")
public class UserController {

    private final UserService userService;

    @Operation(summary = "Listar usuários", description = "Lista paginada com filtros opcionais por nome e email.")
    @ApiResponse(responseCode = "200", description = "Lista retornada com sucesso")
    @GetMapping
    public ResponseEntity<Page<UserResponse>> getAll(
            @RequestParam(required = false) String name,
            @RequestParam(required = false) String email,
            @PageableDefault(size = 20, sort = "name") Pageable pageable) {

        log.info("GET /users - name={} email={}", name, email);
        return ResponseEntity.ok(userService.getUsersWithFilter(name, email, pageable));
    }

    @Operation(summary = "Buscar usuário por ID")
    @ApiResponse(responseCode = "200", description = "Usuário encontrado")
    @ApiResponse(responseCode = "404", description = "Usuário não encontrado")
    @GetMapping("{id}")
    public ResponseEntity<UserResponse> getById(@PathVariable Long id) {
        log.info("GET /users/{}", id);
        return userService.getUserById(id)
            .map(ResponseEntity::ok)
            .orElse(ResponseEntity.notFound().build());
    }

    @Operation(summary = "Criar usuário", description = "ADMIN cria usuário com qualquer role.")
    @ApiResponse(responseCode = "201", description = "Usuário criado")
    @ApiResponse(responseCode = "409", description = "Email já cadastrado")
    @PostMapping
    public ResponseEntity<UserResponse> create(@Valid @RequestBody User user) {
        log.info("POST /users : {} ({})", user.getEmail(), user.getRole());
        UserResponse saved = userService.createUser(user);
        return ResponseEntity.status(201).body(saved);
    }

    @Operation(summary = "Atualizar usuário")
    @ApiResponse(responseCode = "200", description = "Usuário atualizado")
    @ApiResponse(responseCode = "404", description = "Usuário não encontrado")
    @ApiResponse(responseCode = "409", description = "Email já cadastrado")
    @PutMapping("{id}")
    public ResponseEntity<UserResponse> update(@PathVariable Long id, @Valid @RequestBody User userDetails) {
        log.info("PUT /users/{}", id);
        return userService.updateUser(id, userDetails)
            .map(ResponseEntity::ok)
            .orElse(ResponseEntity.notFound().build());
    }

    @Operation(summary = "Deleter usuário")
    @ApiResponse(responseCode = "204", description = "Usuário deletado")
    @ApiResponse(responseCode = "404", description = "Usuário não encontrado")
    @DeleteMapping("{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        log.info("DELETE /users/{}", id);
        boolean deleted = userService.deleteUser(id);
        return deleted ? ResponseEntity.noContent().build() : ResponseEntity.notFound().build();
    }
}
