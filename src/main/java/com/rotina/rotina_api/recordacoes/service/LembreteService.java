package com.rotina.rotina_api.recordacoes.service;

import com.rotina.rotina_api.recordacoes.model.Lembrete;
import com.rotina.rotina_api.recordacoes.model.dto.LembreteRequestDTO;
import com.rotina.rotina_api.recordacoes.model.mapper.LembreteMapper;
import com.rotina.rotina_api.recordacoes.repository.LembreteRepository;
import com.rotina.rotina_api.shared.exception.RecursoNaoEncontradoException;
import com.rotina.rotina_api.shared.storage.SupabaseStorageService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class LembreteService {

    private final LembreteRepository lembreteRepository;
    private final LembreteMapper lembreteMapper;
    private final SupabaseStorageService supabaseStorageService;

    public SupabaseStorageService.UrlDeUpload gerarUrlDeUpload(Long usuarioId, String nomeArquivo) {
        String nomeSanitizado = SupabaseStorageService.sanitizarNomeArquivo(nomeArquivo);
        String caminho = "lembretes/%d/%d-%s".formatted(usuarioId, System.currentTimeMillis(), nomeSanitizado);
        return supabaseStorageService.gerarUrlDeUpload(caminho);
    }

    @Transactional
    public Lembrete cadastrar(Lembrete lembrete) {
        return lembreteRepository.save(lembrete);
    }

    public List<Lembrete> listar(Long usuarioId) {
        return lembreteRepository.findByUsuarioIdOrderByData(usuarioId);
    }

    public Lembrete buscarPorId(Long id, Long usuarioId) {
        return lembreteRepository.findByIdAndUsuarioId(id, usuarioId)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Lembrete não encontrado."));
    }

    @Transactional
    public Lembrete atualizar(Long id, Long usuarioId, LembreteRequestDTO dto) {
        var lembrete = buscarPorId(id, usuarioId);
        lembreteMapper.atualizarEntity(dto, lembrete);
        return lembreteRepository.save(lembrete);
    }

    @Transactional
    public void excluir(Long id, Long usuarioId) {
        var lembrete = buscarPorId(id, usuarioId);
        lembreteRepository.delete(lembrete);
    }
}
