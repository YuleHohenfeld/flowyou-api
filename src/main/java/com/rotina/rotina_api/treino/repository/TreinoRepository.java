package com.rotina.rotina_api.treino.repository;

import com.rotina.rotina_api.treino.model.Treino;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface TreinoRepository extends JpaRepository<Treino, Long> {

    List<Treino> findByUsuarioId(Long usuarioId);

    Optional<Treino> findByIdAndUsuarioId(Long id, Long usuarioId);
}
