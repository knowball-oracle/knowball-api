package br.com.fiap.knowball.dto;

import br.com.fiap.knowball.model.UserRole;

public record UserResponse(
   Long id,
   String name,
   String email,
   UserRole role
) {}
    