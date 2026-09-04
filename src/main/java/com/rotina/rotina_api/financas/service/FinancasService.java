package com.rotina.rotina_api.financas.service;

import com.rotina.rotina_api.financas.model.Categoria;
import com.rotina.rotina_api.financas.model.Lancamento;
import com.rotina.rotina_api.financas.model.TipoLancamento;
import com.rotina.rotina_api.financas.model.dto.ResumoCategoriaDTO;
import com.rotina.rotina_api.financas.model.dto.ResumoFinanceiroResponseDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.YearMonth;
import java.util.List;

@Service
@RequiredArgsConstructor
public class FinancasService {

    private final PerfilFinanceiroService perfilFinanceiroService;
    private final CategoriaService categoriaService;
    private final LancamentoService lancamentoService;

    public ResumoFinanceiroResponseDTO gerarResumo(Long usuarioId, YearMonth mes) {
        var perfil = perfilFinanceiroService.buscarPorUsuarioId(usuarioId);
        var lancamentos = lancamentoService.listarPorPeriodo(usuarioId, mes.atDay(1), mes.atEndOfMonth());

        var totalGanhos = somarPorTipo(lancamentos, TipoLancamento.GANHO);
        var totalInvestido = somarPorTipo(lancamentos, TipoLancamento.INVESTIMENTO);
        var totalGasto = somarPorTipo(lancamentos, TipoLancamento.GASTO).add(totalInvestido);
        var saldo = perfil.getSalario().add(totalGanhos).subtract(totalGasto);

        var categorias = categoriaService.listar(usuarioId).stream()
                .map(categoria -> montarResumoCategoria(categoria, lancamentos, totalGasto))
                .filter(resumo -> resumo.limiteMensal() != null || resumo.valorGasto().compareTo(BigDecimal.ZERO) > 0)
                .toList();

        return new ResumoFinanceiroResponseDTO(
                perfil.getSalario(),
                totalGanhos,
                totalGasto,
                totalInvestido,
                saldo,
                perfil.getLimiteCartaoCredito(),
                categorias
        );
    }

    private ResumoCategoriaDTO montarResumoCategoria(Categoria categoria, List<Lancamento> lancamentos, BigDecimal totalGasto) {
        var valorGasto = lancamentos.stream()
                .filter(lancamento -> lancamento.getCategoriaId().equals(categoria.getId()))
                .filter(lancamento -> lancamento.getTipo() != TipoLancamento.GANHO)
                .map(Lancamento::getValor)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        var limiteMensal = categoria.getLimiteMensal();
        var disponivel = limiteMensal != null ? limiteMensal.subtract(valorGasto) : null;

        var percentualDoTotal = totalGasto.compareTo(BigDecimal.ZERO) > 0
                ? valorGasto.divide(totalGasto, 4, RoundingMode.HALF_UP).multiply(BigDecimal.valueOf(100)).doubleValue()
                : 0.0;

        return new ResumoCategoriaDTO(categoria.getId(), categoria.getNome(), valorGasto, limiteMensal, disponivel, percentualDoTotal);
    }

    private BigDecimal somarPorTipo(List<Lancamento> lancamentos, TipoLancamento tipo) {
        return lancamentos.stream()
                .filter(lancamento -> lancamento.getTipo() == tipo)
                .map(Lancamento::getValor)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }
}
