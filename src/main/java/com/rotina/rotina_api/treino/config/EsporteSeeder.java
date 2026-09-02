package com.rotina.rotina_api.treino.config;

import com.rotina.rotina_api.treino.model.Esporte;
import com.rotina.rotina_api.treino.repository.EsporteRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class EsporteSeeder implements CommandLineRunner {

    private static final List<String> ESPORTES_PADRAO = List.of(
            "Academia", "Corrida", "Pular Corda", "Calistenia"
    );

    private final EsporteRepository esporteRepository;

    @Override
    public void run(String... args) {
        for (String nome : ESPORTES_PADRAO) {
            if (!esporteRepository.existsByNome(nome)) {
                esporteRepository.save(Esporte.builder().nome(nome).build());
            }
        }
    }
}
