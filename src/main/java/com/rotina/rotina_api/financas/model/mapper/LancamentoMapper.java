package com.rotina.rotina_api.financas.model.mapper;

import com.rotina.rotina_api.financas.model.Lancamento;
import com.rotina.rotina_api.financas.model.dto.LancamentoRequestDTO;
import com.rotina.rotina_api.financas.model.dto.LancamentoResponseDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface LancamentoMapper {

    @Mapping(target = "id", ignore = true)
    Lancamento toEntity(LancamentoRequestDTO dto, Long usuarioId);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "usuarioId", ignore = true)
    void atualizarEntity(LancamentoRequestDTO dto, @MappingTarget Lancamento lancamento);

    LancamentoResponseDTO toResponseDTO(Lancamento lancamento, String categoriaNome);
}
