package com.rotina.rotina_api.auth.controller;

import com.rotina.rotina_api.usuario.model.dto.UsuarioCadastroRequestDTO;
import com.rotina.rotina_api.usuario.model.dto.UsuarioResponseDTO;
import com.rotina.rotina_api.usuario.model.mapper.UsuarioMapper;
import com.rotina.rotina_api.usuario.service.UsuarioService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final UsuarioService usuarioService;
    private final UsuarioMapper usuarioMapper;

    @PostMapping("/cadastro")
    public ResponseEntity<UsuarioResponseDTO> cadastrar(@Valid @RequestBody UsuarioCadastroRequestDTO dto) {
        var usuario = usuarioMapper.toEntity(dto);
        var usuarioSalvo = usuarioService.cadastrar(usuario);
        return ResponseEntity.status(HttpStatus.CREATED).body(usuarioMapper.toResponseDTO(usuarioSalvo));
    }
}
