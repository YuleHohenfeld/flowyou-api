package com.rotina.rotina_api.recordacoes.model.mapper;

import com.rotina.rotina_api.recordacoes.model.Lembrete;
import com.rotina.rotina_api.recordacoes.model.dto.LembreteRequestDTO;
import com.rotina.rotina_api.recordacoes.model.dto.LembreteResponseDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface LembreteMapper {

    @Mapping(target = "id", ignore = true)
    Lembrete toEntity(LembreteRequestDTO dto, Long usuarioId);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "usuarioId", ignore = true)
    void atualizarEntity(LembreteRequestDTO dto, @MappingTarget Lembrete lembrete);

    LembreteResponseDTO toResponseDTO(Lembrete lembrete);
}
