package com.rotina.rotina_api.recordacoes.service;

import com.rotina.rotina_api.recordacoes.model.Recordacao;
import com.rotina.rotina_api.recordacoes.model.dto.RecordacaoRequestDTO;
import com.rotina.rotina_api.recordacoes.model.mapper.RecordacaoMapper;
import com.rotina.rotina_api.recordacoes.repository.RecordacaoRepository;
import com.rotina.rotina_api.shared.exception.RecursoNaoEncontradoException;
import com.rotina.rotina_api.shared.storage.SupabaseStorageService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class RecordacaoService {

    private final RecordacaoRepository recordacaoRepository;
    private final RecordacaoMapper recordacaoMapper;
    private final SupabaseStorageService supabaseStorageService;

    public SupabaseStorageService.UrlDeUpload gerarUrlDeUpload(Long usuarioId, String nomeArquivo) {
        String nomeSanitizado = SupabaseStorageService.sanitizarNomeArquivo(nomeArquivo);
        String caminho = "recordacoes/%d/%d-%s".formatted(usuarioId, System.currentTimeMillis(), nomeSanitizado);
        return supabaseStorageService.gerarUrlDeUpload(caminho);
    }

    @Transactional
    public Recordacao cadastrar(Recordacao recordacao) {
        return recordacaoRepository.save(recordacao);
    }

    public List<Recordacao> listar(Long usuarioId, String categoria) {
        return categoria != null
                ? recordacaoRepository.findByUsuarioIdAndCategoria(usuarioId, categoria)
                : recordacaoRepository.findByUsuarioId(usuarioId);
    }

    public Recordacao buscarPorId(Long id, Long usuarioId) {
        return recordacaoRepository.findByIdAndUsuarioId(id, usuarioId)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Recordação não encontrada."));
    }

    @Transactional
    public Recordacao atualizar(Long id, Long usuarioId, RecordacaoRequestDTO dto) {
        var recordacao = buscarPorId(id, usuarioId);
        recordacaoMapper.atualizarEntity(dto, recordacao);
        return recordacaoRepository.save(recordacao);
    }

    @Transactional
    public void excluir(Long id, Long usuarioId) {
        var recordacao = buscarPorId(id, usuarioId);
        recordacaoRepository.delete(recordacao);
    }
}
