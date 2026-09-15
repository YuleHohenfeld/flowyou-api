package com.rotina.rotina_api.estudo.repository;

import com.rotina.rotina_api.estudo.model.Anotacao;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface AnotacaoRepository extends JpaRepository<Anotacao, Long> {

    Optional<Anotacao> findByIdAndUsuarioId(Long id, Long usuarioId);

    List<Anotacao> findByUsuarioIdAndPastaId(Long usuarioId, Long pastaId);

    List<Anotacao> findByUsuarioIdAndPastaIdIsNull(Long usuarioId);
}
