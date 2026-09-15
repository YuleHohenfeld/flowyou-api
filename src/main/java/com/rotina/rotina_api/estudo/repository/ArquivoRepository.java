package com.rotina.rotina_api.estudo.repository;

import com.rotina.rotina_api.estudo.model.Arquivo;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ArquivoRepository extends JpaRepository<Arquivo, Long> {

    Optional<Arquivo> findByIdAndUsuarioId(Long id, Long usuarioId);

    List<Arquivo> findByUsuarioIdAndPastaId(Long usuarioId, Long pastaId);

    List<Arquivo> findByUsuarioIdAndPastaIdIsNull(Long usuarioId);
}
