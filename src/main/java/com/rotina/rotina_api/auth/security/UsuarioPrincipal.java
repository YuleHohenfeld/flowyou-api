package com.rotina.rotina_api.auth.security;

import com.rotina.rotina_api.usuario.model.Usuario;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Collections;

public class UsuarioPrincipal implements UserDetails {

    private final Long usuarioId;
    private final String email;
    private final String senhaHash;
    private final boolean ativo;

    public UsuarioPrincipal(Usuario usuario) {
        this.usuarioId = usuario.getId();
        this.email = usuario.getEmail();
        this.senhaHash = usuario.getSenhaHash();
        this.ativo = usuario.isAtivo();
    }

    public Long getUsuarioId() {
        return usuarioId;
    }

    @Override
    public String getPassword() {
        return senhaHash;
    }

    @Override
    public String getUsername() {
        return email;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return Collections.emptyList();
    }

    @Override
    public boolean isEnabled() {
        return ativo;
    }
}
