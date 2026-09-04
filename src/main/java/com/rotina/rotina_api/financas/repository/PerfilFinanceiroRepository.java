package com.rotina.rotina_api.financas.repository;

import com.rotina.rotina_api.financas.model.PerfilFinanceiro;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PerfilFinanceiroRepository extends JpaRepository<PerfilFinanceiro, Long> {

    Optional<PerfilFinanceiro> findByUsuarioId(Long usuarioId);
}
