package com.rotina.rotina_api.recordacoes.repository;

import com.rotina.rotina_api.recordacoes.model.Lembrete;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface LembreteRepository extends JpaRepository<Lembrete, Long> {

    Optional<Lembrete> findByIdAndUsuarioId(Long id, Long usuarioId);

    List<Lembrete> findByUsuarioIdOrderByData(Long usuarioId);
}
