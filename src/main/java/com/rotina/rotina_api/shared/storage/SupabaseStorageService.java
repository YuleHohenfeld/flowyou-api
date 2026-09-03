package com.rotina.rotina_api.shared.storage;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.Map;

@Service
public class SupabaseStorageService {

    private final RestClient restClient;
    private final String supabaseUrl;
    private final String bucket;

    public SupabaseStorageService(@Value("${app.supabase.url:}") String supabaseUrl,
                                   @Value("${app.supabase.jwt-secret:}") String jwtSecret,
                                   @Value("${app.supabase.bucket:midias}") String bucket) {
        this.supabaseUrl = supabaseUrl;
        this.bucket = bucket;
        String serviceRoleToken = jwtSecret.isBlank() ? "" : gerarTokenServiceRole(jwtSecret);
        this.restClient = RestClient.builder()
                .baseUrl(supabaseUrl + "/storage/v1")
                .defaultHeader("Authorization", "Bearer " + serviceRoleToken)
                .defaultHeader("apikey", serviceRoleToken)
                .build();
    }

    private String gerarTokenServiceRole(String jwtSecret) {
        var chave = Keys.hmacShaKeyFor(jwtSecret.getBytes());
        Instant agora = Instant.now();
        return Jwts.builder()
                .claim("role", "service_role")
                .issuer("supabase")
                .issuedAt(Date.from(agora))
                .expiration(Date.from(agora.plus(3650, ChronoUnit.DAYS)))
                .signWith(chave)
                .compact();
    }

    public UrlDeUpload gerarUrlDeUpload(String caminho) {
        var resposta = restClient.post()
                .uri("/object/upload/sign/{bucket}/{caminho}", bucket, caminho)
                .body(Map.of())
                .retrieve()
                .body(RespostaAssinatura.class);

        String uploadUrl = supabaseUrl + "/storage/v1" + resposta.url();
        String midiaPath = supabaseUrl + "/storage/v1/object/public/" + bucket + "/" + caminho;
        return new UrlDeUpload(uploadUrl, midiaPath);
    }

    private record RespostaAssinatura(String url) {
    }

    public record UrlDeUpload(String uploadUrl, String midiaPath) {
    }
}
