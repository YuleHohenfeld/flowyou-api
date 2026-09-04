package com.rotina.rotina_api.auth.security;

import com.rotina.rotina_api.auth.model.RefreshToken;
import com.rotina.rotina_api.auth.repository.RefreshTokenRepository;
import com.rotina.rotina_api.shared.exception.CredenciaisInvalidasException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Base64;

@Component
public class RefreshTokenService {

    private final RefreshTokenRepository refreshTokenRepository;
    private final long expiracaoMs;

    public RefreshTokenService(RefreshTokenRepository refreshTokenRepository,
                                @Value("${app.refresh-token.expiration-ms}") long expiracaoMs) {
        this.refreshTokenRepository = refreshTokenRepository;
        this.expiracaoMs = expiracaoMs;
    }

    public String gerar(Long usuarioId) {
        String tokenBruto = gerarTokenAleatorio();
        var refreshToken = RefreshToken.builder()
                .usuarioId(usuarioId)
                .tokenHash(hash(tokenBruto))
                .expiraEm(LocalDateTime.now().plus(expiracaoMs, ChronoUnit.MILLIS))
                .build();
        refreshTokenRepository.save(refreshToken);
        return tokenBruto;
    }

    @Transactional
    public Long validarERevogar(String tokenBruto) {
        var refreshToken = refreshTokenRepository.findByTokenHash(hash(tokenBruto))
                .orElseThrow(() -> new CredenciaisInvalidasException("Refresh token inválido ou expirado."));

        refreshTokenRepository.delete(refreshToken);

        if (refreshToken.getExpiraEm().isBefore(LocalDateTime.now())) {
            throw new CredenciaisInvalidasException("Refresh token inválido ou expirado.");
        }

        return refreshToken.getUsuarioId();
    }

    private String gerarTokenAleatorio() {
        byte[] bytes = new byte[32];
        new SecureRandom().nextBytes(bytes);
        return Base64.getUrlEncoder().withoutPadding().encodeToString(bytes);
    }

    private String hash(String tokenBruto) {
        try {
            var digest = MessageDigest.getInstance("SHA-256");
            byte[] hash = digest.digest(tokenBruto.getBytes(StandardCharsets.UTF_8));
            return Base64.getEncoder().encodeToString(hash);
        } catch (NoSuchAlgorithmException e) {
            throw new IllegalStateException(e);
        }
    }
}
