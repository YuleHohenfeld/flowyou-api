package com.rotina.rotina_api.usuario.model.dto;

import java.time.LocalDateTime;

public record UsuarioResponseDTO(
        Long id,
        String nome,
        String email,
        LocalDateTime dataCriacao,
        boolean ativo
) {
}
