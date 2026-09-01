package com.rotina.rotina_api.usuario.controller;

import com.rotina.rotina_api.auth.security.UsuarioPrincipal;
import com.rotina.rotina_api.shared.exception.AcessoNegadoException;
import com.rotina.rotina_api.usuario.model.dto.UsuarioResponseDTO;
import com.rotina.rotina_api.usuario.model.mapper.UsuarioMapper;
import com.rotina.rotina_api.usuario.service.UsuarioService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/usuarios")
@RequiredArgsConstructor
public class UsuarioController {

    private final UsuarioService usuarioService;
    private final UsuarioMapper usuarioMapper;

    @GetMapping("/{id}")
    public UsuarioResponseDTO buscarPorId(@PathVariable Long id, @AuthenticationPrincipal UsuarioPrincipal principal) {
        if (!principal.getUsuarioId().equals(id)) {
            throw new AcessoNegadoException("Você só pode acessar o próprio perfil.");
        }

        return usuarioMapper.toResponseDTO(usuarioService.buscarPorId(id));
    }
}
