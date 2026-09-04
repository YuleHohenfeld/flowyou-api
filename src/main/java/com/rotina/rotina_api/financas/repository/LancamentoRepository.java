package com.rotina.rotina_api.financas.repository;

import com.rotina.rotina_api.financas.model.Lancamento;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface LancamentoRepository extends JpaRepository<Lancamento, Long> {

    List<Lancamento> findByUsuarioId(Long usuarioId);

    Optional<Lancamento> findByIdAndUsuarioId(Long id, Long usuarioId);

    List<Lancamento> findByUsuarioIdAndDataBetween(Long usuarioId, LocalDate inicio, LocalDate fim);

    boolean existsByCategoriaId(Long categoriaId);
}
