package com.comunicafacil.service;

import com.comunicafacil.model.*;
import com.comunicafacil.repository.ConteudoRepository;
import com.comunicafacil.repository.SolicitacaoRepository;
import com.comunicafacil.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class SolicitacaoService {

    @Autowired
    private SolicitacaoRepository solicitacaoRepository;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private ConteudoRepository conteudoRepository;

    @Autowired
    private ConversaoService conversaoService;

    public Solicitacao criarSolicitacao(Long usuarioId, Long conteudoId, List<FormatoSaida> formatos) {

        UsuarioAbstrato usuario = usuarioRepository.findById(usuarioId)
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));

        Conteudo conteudo = conteudoRepository.findById(conteudoId)
                .orElseThrow(() -> new RuntimeException("Conteúdo não encontrado"));

        Solicitacao solicitacao = new Solicitacao();
        solicitacao.setUsuario(usuario);
        solicitacao.setConteudo(conteudo);
        solicitacao.setFormatosSolicitados(formatos);
        solicitacao.setDataCriacao(LocalDateTime.now());

        Solicitacao saved = solicitacaoRepository.save(solicitacao);

        conversaoService.gerarConversoesParaSolicitacao(saved);

        return saved;
    }

    public Solicitacao buscarPorId(Long id) {
        return solicitacaoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Solicitação não encontrada"));
    }

    public List<Solicitacao> listarTodas() {
        return solicitacaoRepository.findAll();
    }
}
