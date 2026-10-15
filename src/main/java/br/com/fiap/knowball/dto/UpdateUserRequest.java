package br.com.fiap.knowball.dto;

import br.com.fiap.knowball.model.UserRole;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record UpdateUserRequest(

        @NotBlank(message = "O nome é obrigatório.")
        @Size(max = 100, message = "O nome deve ter no máximo 100 caracteres.")
        String name,

        @NotBlank(message = "O e-mail é obrigatório.")
        @Email(message = "Informe um e-mail válido.")
        @Size(max = 150, message = "O e-mail deve ter no máximo 150 caracteres.")
        String email,

        @Pattern(
                regexp = "(?s)^(?:|(?=.*\\S).{6,})$",
                message = "A nova senha deve ter pelo menos 6 caracteres e não pode conter apenas espaços."
        )
        String password,

        @NotNull(message = "O perfil do usuário é obrigatório.")
        UserRole role

) {
}