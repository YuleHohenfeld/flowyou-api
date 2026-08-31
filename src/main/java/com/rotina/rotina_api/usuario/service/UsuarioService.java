package com.rotina.rotina_api.usuario.service;

import com.rotina.rotina_api.shared.exception.ConflitoException;
import com.rotina.rotina_api.shared.exception.RecursoNaoEncontradoException;
import com.rotina.rotina_api.usuario.model.Usuario;
import com.rotina.rotina_api.usuario.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UsuarioService {

    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;

    public Usuario cadastrar(Usuario usuario) {
        if (usuarioRepository.existsByEmail(usuario.getEmail())) {
            throw new ConflitoException("Já existe um usuário cadastrado com este e-mail.");
        }
        usuario.setSenhaHash(passwordEncoder.encode(usuario.getSenhaHash()));
        return usuarioRepository.save(usuario);
    }

    public Usuario buscarPorId(Long id) {
        return usuarioRepository.findById(id)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Usuário não encontrado."));
    }
}
