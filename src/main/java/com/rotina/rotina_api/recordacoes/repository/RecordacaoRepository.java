package com.rotina.rotina_api.recordacoes.repository;

import com.rotina.rotina_api.recordacoes.model.Recordacao;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface RecordacaoRepository extends JpaRepository<Recordacao, Long> {

    Optional<Recordacao> findByIdAndUsuarioId(Long id, Long usuarioId);

    List<Recordacao> findByUsuarioId(Long usuarioId);

    List<Recordacao> findByUsuarioIdAndCategoria(Long usuarioId, String categoria);
}
