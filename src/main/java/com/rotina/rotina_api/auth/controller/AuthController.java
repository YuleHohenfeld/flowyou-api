package com.rotina.rotina_api.auth.controller;

import com.rotina.rotina_api.auth.model.dto.LoginRequestDTO;
import com.rotina.rotina_api.auth.model.dto.TokenResponseDTO;
import com.rotina.rotina_api.auth.security.JwtUtil;
import com.rotina.rotina_api.shared.exception.CredenciaisInvalidasException;
import com.rotina.rotina_api.usuario.model.dto.UsuarioCadastroRequestDTO;
import com.rotina.rotina_api.usuario.model.dto.UsuarioResponseDTO;
import com.rotina.rotina_api.usuario.model.mapper.UsuarioMapper;
import com.rotina.rotina_api.usuario.repository.UsuarioRepository;
import com.rotina.rotina_api.usuario.service.UsuarioService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
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
    private final UsuarioRepository usuarioRepository;
    private final AuthenticationManager authenticationManager;
    private final JwtUtil jwtUtil;

    @PostMapping("/cadastro")
    public ResponseEntity<UsuarioResponseDTO> cadastrar(@Valid @RequestBody UsuarioCadastroRequestDTO dto) {
        var usuario = usuarioMapper.toEntity(dto);
        var usuarioSalvo = usuarioService.cadastrar(usuario);
        return ResponseEntity.status(HttpStatus.CREATED).body(usuarioMapper.toResponseDTO(usuarioSalvo));
    }

    @PostMapping("/login")
    public TokenResponseDTO login(@Valid @RequestBody LoginRequestDTO dto) {
        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(dto.email(), dto.senha())
            );
        } catch (BadCredentialsException e) {
            throw new CredenciaisInvalidasException("E-mail ou senha inválidos.");
        }

        var usuario = usuarioRepository.findByEmail(dto.email()).orElseThrow();
        return new TokenResponseDTO(jwtUtil.gerarToken(usuario.getEmail(), usuario.getId()));
    }
}
