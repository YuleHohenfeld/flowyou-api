package com.rotina.rotina_api.usuario.model.mapper;

import com.rotina.rotina_api.usuario.model.Usuario;
import com.rotina.rotina_api.usuario.model.dto.UsuarioCadastroRequestDTO;
import com.rotina.rotina_api.usuario.model.dto.UsuarioResponseDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface UsuarioMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "senhaHash", source = "senha")
    @Mapping(target = "dataCriacao", ignore = true)
    @Mapping(target = "ativo", ignore = true)
    Usuario toEntity(UsuarioCadastroRequestDTO dto);

    UsuarioResponseDTO toResponseDTO(Usuario usuario);
}
