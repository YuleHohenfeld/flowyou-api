package com.rotina.rotina_api.estudo.service;

import com.rotina.rotina_api.estudo.model.Arquivo;
import com.rotina.rotina_api.estudo.repository.ArquivoRepository;
import com.rotina.rotina_api.shared.exception.RecursoNaoEncontradoException;
import com.rotina.rotina_api.shared.storage.SupabaseStorageService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ArquivoService {

    private final ArquivoRepository arquivoRepository;
    private final PastaService pastaService;
    private final SupabaseStorageService supabaseStorageService;

    public SupabaseStorageService.UrlDeUpload gerarUrlDeUpload(Long usuarioId, String nomeArquivo) {
        String caminho = "arquivos/%d/%d-%s".formatted(usuarioId, System.currentTimeMillis(), nomeArquivo);
        return supabaseStorageService.gerarUrlDeUpload(caminho);
    }

    @Transactional
    public Arquivo cadastrar(Arquivo arquivo) {
        validarPasta(arquivo.getUsuarioId(), arquivo.getPastaId());
        return arquivoRepository.save(arquivo);
    }

    public List<Arquivo> listarPorPasta(Long usuarioId, Long pastaId) {
        pastaService.buscarPorId(pastaId, usuarioId);
        return arquivoRepository.findByUsuarioIdAndPastaId(usuarioId, pastaId);
    }

    public List<Arquivo> listarSoltos(Long usuarioId) {
        return arquivoRepository.findByUsuarioIdAndPastaIdIsNull(usuarioId);
    }

    public Arquivo buscarPorId(Long id, Long usuarioId) {
        return arquivoRepository.findByIdAndUsuarioId(id, usuarioId)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Arquivo não encontrado."));
    }

    @Transactional
    public void excluir(Long id, Long usuarioId) {
        var arquivo = buscarPorId(id, usuarioId);
        arquivoRepository.delete(arquivo);
    }

    private void validarPasta(Long usuarioId, Long pastaId) {
        if (pastaId != null) {
            pastaService.buscarPorId(pastaId, usuarioId);
        }
    }
}
