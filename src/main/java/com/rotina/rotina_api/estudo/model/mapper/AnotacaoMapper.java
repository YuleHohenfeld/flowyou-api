package com.rotina.rotina_api.estudo.model.mapper;

import com.rotina.rotina_api.estudo.model.Anotacao;
import com.rotina.rotina_api.estudo.model.dto.AnotacaoRequestDTO;
import com.rotina.rotina_api.estudo.model.dto.AnotacaoResponseDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface AnotacaoMapper {

    @Mapping(target = "id", ignore = true)
    Anotacao toEntity(AnotacaoRequestDTO dto, Long usuarioId);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "usuarioId", ignore = true)
    void atualizarEntity(AnotacaoRequestDTO dto, @MappingTarget Anotacao anotacao);

    AnotacaoResponseDTO toResponseDTO(Anotacao anotacao);
}
