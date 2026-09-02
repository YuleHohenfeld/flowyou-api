package com.rotina.rotina_api.treino.model.mapper;

import com.rotina.rotina_api.treino.model.Exercicio;
import com.rotina.rotina_api.treino.model.dto.ExercicioRequestDTO;
import com.rotina.rotina_api.treino.model.dto.ExercicioResponseDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface ExercicioMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "midiaPath", ignore = true)
    Exercicio toEntity(ExercicioRequestDTO dto, Long treinoId, Integer ordem);

    ExercicioResponseDTO toResponseDTO(Exercicio exercicio);
}
