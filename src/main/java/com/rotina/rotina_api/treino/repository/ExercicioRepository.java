package com.rotina.rotina_api.treino.repository;

import com.rotina.rotina_api.treino.model.Exercicio;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ExercicioRepository extends JpaRepository<Exercicio, Long> {

    List<Exercicio> findByTreinoIdOrderByOrdem(Long treinoId);
}
