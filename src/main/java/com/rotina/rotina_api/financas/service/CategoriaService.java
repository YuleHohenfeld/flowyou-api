package com.rotina.rotina_api.financas.service;

import com.rotina.rotina_api.financas.model.Categoria;
import com.rotina.rotina_api.financas.model.dto.CategoriaRequestDTO;
import com.rotina.rotina_api.financas.model.mapper.CategoriaMapper;
import com.rotina.rotina_api.financas.repository.CategoriaRepository;
import com.rotina.rotina_api.financas.repository.LancamentoRepository;
import com.rotina.rotina_api.shared.exception.ConflitoException;
import com.rotina.rotina_api.shared.exception.RecursoNaoEncontradoException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CategoriaService {

    private final CategoriaRepository categoriaRepository;
    private final CategoriaMapper categoriaMapper;
    private final LancamentoRepository lancamentoRepository;

    @Transactional
    public Categoria criar(Categoria categoria) {
        return categoriaRepository.save(categoria);
    }

    public List<Categoria> listar(Long usuarioId) {
        return categoriaRepository.findByUsuarioId(usuarioId);
    }

    public Categoria buscarPorId(Long id, Long usuarioId) {
        return categoriaRepository.findByIdAndUsuarioId(id, usuarioId)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Categoria não encontrada."));
    }

    @Transactional
    public Categoria atualizar(Long id, Long usuarioId, CategoriaRequestDTO dto) {
        var categoria = buscarPorId(id, usuarioId);
        categoriaMapper.atualizarEntity(dto, categoria);
        return categoriaRepository.save(categoria);
    }

    @Transactional
    public void excluir(Long id, Long usuarioId) {
        var categoria = buscarPorId(id, usuarioId);

        if (lancamentoRepository.existsByCategoriaId(id)) {
            throw new ConflitoException("Categoria possui lançamentos vinculados e não pode ser excluída.");
        }

        categoriaRepository.delete(categoria);
    }
}
