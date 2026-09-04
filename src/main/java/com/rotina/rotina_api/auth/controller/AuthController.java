package com.rotina.rotina_api.auth.controller;

import com.rotina.rotina_api.auth.model.dto.LoginRequestDTO;
import com.rotina.rotina_api.auth.model.dto.RefreshRequestDTO;
import com.rotina.rotina_api.auth.model.dto.TokenResponseDTO;
import com.rotina.rotina_api.auth.security.JwtUtil;
import com.rotina.rotina_api.auth.security.RefreshTokenService;
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
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Locale;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final UsuarioService usuarioService;
    private final UsuarioMapper usuarioMapper;
    private final UsuarioRepository usuarioRepository;
    private final AuthenticationManager authenticationManager;
    private final JwtUtil jwtUtil;
    private final RefreshTokenService refreshTokenService;

    @PostMapping("/cadastro")
    public ResponseEntity<UsuarioResponseDTO> cadastrar(@Valid @RequestBody UsuarioCadastroRequestDTO dto) {
        var usuario = usuarioMapper.toEntity(dto);
        var usuarioSalvo = usuarioService.cadastrar(usuario);
        return ResponseEntity.status(HttpStatus.CREATED).body(usuarioMapper.toResponseDTO(usuarioSalvo));
    }

    @PostMapping("/login")
    public TokenResponseDTO login(@Valid @RequestBody LoginRequestDTO dto) {
        String email = dto.email().trim().toLowerCase(Locale.ROOT);

        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(email, dto.senha())
            );
        } catch (AuthenticationException e) {
            throw new CredenciaisInvalidasException("E-mail ou senha inválidos.");
        }

        var usuario = usuarioRepository.findByEmail(email)
                .orElseThrow(() -> new CredenciaisInvalidasException("E-mail ou senha inválidos."));

        String token = jwtUtil.gerarToken(usuario.getId(), usuario.getEmail());
        String refreshToken = refreshTokenService.gerar(usuario.getId());
        return new TokenResponseDTO(token, refreshToken);
    }

    @PostMapping("/refresh")
    public TokenResponseDTO refresh(@Valid @RequestBody RefreshRequestDTO dto) {
        Long usuarioId = refreshTokenService.validarERevogar(dto.refreshToken());

        var usuario = usuarioRepository.findById(usuarioId)
                .orElseThrow(() -> new CredenciaisInvalidasException("Refresh token inválido ou expirado."));

        String novoToken = jwtUtil.gerarToken(usuario.getId(), usuario.getEmail());
        String novoRefreshToken = refreshTokenService.gerar(usuario.getId());
        return new TokenResponseDTO(novoToken, novoRefreshToken);
    }
}
