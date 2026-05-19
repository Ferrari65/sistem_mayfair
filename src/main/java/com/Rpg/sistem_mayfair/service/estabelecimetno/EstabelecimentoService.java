package com.Rpg.sistem_mayfair.service.estabelecimetno;

import com.Rpg.sistem_mayfair.domain.Personagem;
import com.Rpg.sistem_mayfair.domain.estabelecimento.Estabelecimento;
import com.Rpg.sistem_mayfair.domain.estabelecimento.FotoEstabelecimento;
import com.Rpg.sistem_mayfair.domain.estabelecimento.MovimentacaoEstabelecimento;
import com.Rpg.sistem_mayfair.dto.estabelecimento.EstabelecimentoDTO;
import com.Rpg.sistem_mayfair.dto.estabelecimento.RegistrarMovimentacaoDTO;
import com.Rpg.sistem_mayfair.repository.PersonagemRepository;
import com.Rpg.sistem_mayfair.repository.estabelecimento.EstabelecimentoRepository;
import com.Rpg.sistem_mayfair.repository.estabelecimento.MovimentacaoEstabelecimentoRepository;
import com.Rpg.sistem_mayfair.service.CloudinaryService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class EstabelecimentoService {

    private final EstabelecimentoRepository repository;
    private final PersonagemRepository personagemRepository;
    private final CloudinaryService cloudinaryService;
    private final MovimentacaoEstabelecimentoRepository movimentacaoRepository;

    /*
     * =========================
     * CONVERSOR ENTITY -> DTO
     * =========================
     */
    private EstabelecimentoDTO converterParaDTO(Estabelecimento entidade) {

        EstabelecimentoDTO dto = new EstabelecimentoDTO();

        dto.setId(entidade.getId());
        dto.setNomeLocal(entidade.getNomeLocal());
        dto.setDescricao(entidade.getDescricao());
        dto.setMoral(entidade.getMoral());
        dto.setDinheiro(entidade.getDinheiro());
        dto.setHorarioAbertura(entidade.getHorarioAbertura());
        dto.setHorarioFechamento(entidade.getHorarioFechamento());

        /*
         * PROPRIETÁRIO
         */
        dto.setProprietarioId(
                entidade.getProprietario() != null
                        ? entidade.getProprietario().getId_personagens()
                        : null
        );

        /*
         * FUNCIONÁRIOS
         */
        dto.setFuncionariosIds(
                entidade.getFuncionarios()
                        .stream()
                        .map(Personagem::getId_personagens)
                        .collect(Collectors.toList())
        );

        /*
         * FOTO
         */
        List<String> fotos = new ArrayList<>();

        if (
                entidade.getFotos() != null &&
                        entidade.getFotos().getImageUrl() != null
        ) {
            fotos.add(entidade.getFotos().getImageUrl());
        }

        dto.setFotos(fotos);

        return dto;
    }

    /*
     * =========================
     * BUSCAR ENTIDADE
     * =========================
     */
    private Estabelecimento buscarEntidade(Long id) {

        return repository.findById(id)
                .orElseThrow(() ->
                        new RuntimeException("Estabelecimento não encontrado")
                );
    }

    /*
     * =========================
     * CRIAR
     * =========================
     */
    @Transactional
    public EstabelecimentoDTO criar(EstabelecimentoDTO dto) {

        Personagem proprietario = personagemRepository
                .findById(dto.getProprietarioId())
                .orElseThrow(() ->
                        new RuntimeException("Proprietário não encontrado")
                );

        List<Personagem> funcionarios =
                dto.getFuncionariosIds() != null
                        ? personagemRepository.findAllById(dto.getFuncionariosIds())
                        : new ArrayList<>();

        Estabelecimento estabelecimento = Estabelecimento.builder()
                .nomeLocal(dto.getNomeLocal())
                .descricao(dto.getDescricao())
                .moral(dto.getMoral() != null ? dto.getMoral() : 50)
                .dinheiro(dto.getDinheiro() != null ? dto.getDinheiro() : 0.0)
                .horarioAbertura(dto.getHorarioAbertura())
                .horarioFechamento(dto.getHorarioFechamento())
                .proprietario(proprietario)
                .funcionarios(funcionarios)
                .build();

        repository.save(estabelecimento);

        return converterParaDTO(estabelecimento);
    }

    /*
     * =========================
     * ATUALIZAR
     * =========================
     */
    @Transactional
    public EstabelecimentoDTO atualizar(Long id, EstabelecimentoDTO dto) {

        Estabelecimento estabelecimento = buscarEntidade(id);

        /*
         * PRESERVA FOTO ATUAL
         */
        FotoEstabelecimento fotoAtual = estabelecimento.getFotos();

        Personagem proprietario = personagemRepository
                .findById(dto.getProprietarioId())
                .orElseThrow(() ->
                        new RuntimeException("Proprietário não encontrado")
                );

        List<Personagem> funcionarios =
                dto.getFuncionariosIds() != null
                        ? personagemRepository.findAllById(dto.getFuncionariosIds())
                        : new ArrayList<>();

        estabelecimento.setNomeLocal(dto.getNomeLocal());
        estabelecimento.setDescricao(dto.getDescricao());

        estabelecimento.setHorarioAbertura(dto.getHorarioAbertura());
        estabelecimento.setHorarioFechamento(dto.getHorarioFechamento());

        estabelecimento.setMoral(
                dto.getMoral() != null
                        ? dto.getMoral()
                        : estabelecimento.getMoral()
        );

        estabelecimento.setDinheiro(
                dto.getDinheiro() != null
                        ? dto.getDinheiro()
                        : estabelecimento.getDinheiro()
        );

        estabelecimento.setProprietario(proprietario);

        /*
         * FUNCIONÁRIOS
         */
        estabelecimento.setFuncionarios(funcionarios);

        /*
         * RESTAURA FOTO
         */
        estabelecimento.setFotos(fotoAtual);

        repository.save(estabelecimento);

        return converterParaDTO(estabelecimento);
    }

    /*
     * =========================
     * LISTAR TODOS
     * =========================
     */
    public List<EstabelecimentoDTO> listarTodos() {

        return repository.findAll()
                .stream()
                .map(this::converterParaDTO)
                .collect(Collectors.toList());
    }

    /*
     * =========================
     * BUSCAR POR ID
     * =========================
     */
    public EstabelecimentoDTO buscarPorIdDTO(Long id) {

        Estabelecimento estabelecimento = buscarEntidade(id);

        return converterParaDTO(estabelecimento);
    }

    /*
     * =========================
     * ALTERAR MORAL
     * =========================
     */
    @Transactional
    public EstabelecimentoDTO alterarMoral(Long id, int quantidade) {

        Estabelecimento estabelecimento = buscarEntidade(id);

        estabelecimento.alterarMoral(quantidade);

        repository.save(estabelecimento);

        return converterParaDTO(estabelecimento);
    }

    /*
     * =========================
     * ALTERAR DINHEIRO
     * =========================
     */
    @Transactional
    public EstabelecimentoDTO alterarDinheiro(Long id, double valor) {

        Estabelecimento estabelecimento = buscarEntidade(id);

        estabelecimento.alterarDinheiro(valor);

        repository.save(estabelecimento);

        return converterParaDTO(estabelecimento);
    }

    /*
     * =========================
     * DELETAR
     * =========================
     */
    @Transactional
    public void deletar(Long id) {

        Estabelecimento estabelecimento = buscarEntidade(id);

        repository.delete(estabelecimento);
    }

    /*
     * =========================
     * ADICIONAR FOTO
     * =========================
     */
    @Transactional
    public EstabelecimentoDTO adicionarFoto(
            Long estabelecimentoId,
            MultipartFile file
    ) {

        Estabelecimento estabelecimento = buscarEntidade(estabelecimentoId);

        String imageUrl = cloudinaryService.uploadFile(file);

        FotoEstabelecimento foto = estabelecimento.getFotos();

        if (foto == null) {

            foto = new FotoEstabelecimento();

            foto.setEstabelecimento(estabelecimento);

            estabelecimento.setFotos(foto);
        }

        foto.setImageUrl(imageUrl);

        repository.save(estabelecimento);

        return converterParaDTO(estabelecimento);
    }

    /*
     * =========================
     * REGISTRAR MOVIMENTAÇÃO
     * =========================
     */
    @Transactional
    public void registrarMovimentacao(
            Long estabelecimentoId,
            RegistrarMovimentacaoDTO dto
    ) {

        Estabelecimento estabelecimento = repository
                .findById(estabelecimentoId)
                .orElseThrow(() ->
                        new EntityNotFoundException("Estabelecimento não encontrado."));


        int impactoMoral = dto.getImpactoMoral();

        /*
         * VALIDAÇÃO
         */
        if (impactoMoral > 100 || impactoMoral < -100) {

            throw new IllegalArgumentException(
                    "Impacto moral inválido. Limite permitido: -100 até 100."
            );
        }

        /*
         * CRIA MOVIMENTAÇÃO
         */
        MovimentacaoEstabelecimento movimentacao =
                MovimentacaoEstabelecimento.builder()
                        .estabelecimento(estabelecimento)
                        .tipo(dto.getTipo())
                        .impactoMoral(impactoMoral)
                        .observacao(dto.getObservacao())
                        .build();

        movimentacaoRepository.save(movimentacao);

        /*
         * ALTERA MORAL
         */
        estabelecimento.alterarMoral(impactoMoral);

        /*
         * ESTATÍSTICA
         */
        estabelecimento.registrarMovimentacao();

        repository.save(estabelecimento);
    }

}