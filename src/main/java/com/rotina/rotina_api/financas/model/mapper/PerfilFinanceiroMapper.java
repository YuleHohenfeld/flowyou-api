package com.rotina.rotina_api.financas.model.mapper;

import com.rotina.rotina_api.financas.model.PerfilFinanceiro;
import com.rotina.rotina_api.financas.model.dto.PerfilFinanceiroRequestDTO;
import com.rotina.rotina_api.financas.model.dto.PerfilFinanceiroResponseDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface PerfilFinanceiroMapper {

    @Mapping(target = "id", ignore = true)
    PerfilFinanceiro toEntity(PerfilFinanceiroRequestDTO dto, Long usuarioId);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "usuarioId", ignore = true)
    void atualizarEntity(PerfilFinanceiroRequestDTO dto, @MappingTarget PerfilFinanceiro perfil);

    PerfilFinanceiroResponseDTO toResponseDTO(PerfilFinanceiro perfil);
}
