package com.rotina.rotina_api.shared.config;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.ByteArrayInputStream;
import java.util.Base64;

@Slf4j
@Configuration
public class FirebaseConfig {

    @Bean
    public FirebaseApp firebaseApp(@Value("${app.firebase.credentials-base64:}") String credenciaisBase64) {
        if (credenciaisBase64 == null || credenciaisBase64.isBlank()) {
            log.warn("FIREBASE_CREDENTIALS_BASE64 nao configurado - geracao de firebaseToken no login fica desabilitada.");
            return null;
        }

        try {
            byte[] credenciais = Base64.getDecoder().decode(credenciaisBase64);
            var options = FirebaseOptions.builder()
                    .setCredentials(GoogleCredentials.fromStream(new ByteArrayInputStream(credenciais)))
                    .build();
            return FirebaseApp.initializeApp(options);
        } catch (Exception e) {
            log.error("Falha ao inicializar o Firebase Admin SDK - geracao de firebaseToken no login fica desabilitada.", e);
            return null;
        }
    }
}
