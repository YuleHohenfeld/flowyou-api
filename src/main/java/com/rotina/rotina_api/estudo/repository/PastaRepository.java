package com.rotina.rotina_api.estudo.repository;

import com.rotina.rotina_api.estudo.model.Pasta;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface PastaRepository extends JpaRepository<Pasta, Long> {

    Optional<Pasta> findByIdAndUsuarioId(Long id, Long usuarioId);

    List<Pasta> findByUsuarioIdAndPastaPaiIdIsNull(Long usuarioId);

    List<Pasta> findByUsuarioIdAndPastaPaiId(Long usuarioId, Long pastaPaiId);
}
