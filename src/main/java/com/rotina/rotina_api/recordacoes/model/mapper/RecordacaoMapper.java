package com.rotina.rotina_api.recordacoes.model.mapper;

import com.rotina.rotina_api.recordacoes.model.Recordacao;
import com.rotina.rotina_api.recordacoes.model.dto.RecordacaoRequestDTO;
import com.rotina.rotina_api.recordacoes.model.dto.RecordacaoResponseDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface RecordacaoMapper {

    @Mapping(target = "id", ignore = true)
    Recordacao toEntity(RecordacaoRequestDTO dto, Long usuarioId);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "usuarioId", ignore = true)
    void atualizarEntity(RecordacaoRequestDTO dto, @MappingTarget Recordacao recordacao);

    RecordacaoResponseDTO toResponseDTO(Recordacao recordacao);
}
