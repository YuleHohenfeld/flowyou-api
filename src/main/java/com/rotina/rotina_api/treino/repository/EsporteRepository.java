package com.rotina.rotina_api.treino.repository;

import com.rotina.rotina_api.treino.model.Esporte;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EsporteRepository extends JpaRepository<Esporte, Long> {

    boolean existsByNome(String nome);
}
