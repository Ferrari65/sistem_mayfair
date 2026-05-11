package com.Rpg.sistem_mayfair.service.estabelecimetno;

import com.Rpg.sistem_mayfair.domain.Personagem;
import com.Rpg.sistem_mayfair.domain.estabelecimento.Estabelecimento;
import com.Rpg.sistem_mayfair.domain.estabelecimento.FotoEstabelecimento;
import com.Rpg.sistem_mayfair.dto.estabelecimento.EstabelecimentoDTO;
import com.Rpg.sistem_mayfair.repository.PersonagemRepository;
import com.Rpg.sistem_mayfair.repository.estabelecimento.EstabelecimentoRepository;
import com.Rpg.sistem_mayfair.service.CloudinaryService;
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

    /*
     * =========================
     * CONVERSOR (ENTITY -> DTO)
     * =========================
     * Este método garante que o Front-end receba a lista 'fotos' corretamente.
     */
    private EstabelecimentoDTO converterParaDTO(Estabelecimento entidade) {
        EstabelecimentoDTO dto = new EstabelecimentoDTO();
        dto.setNomeLocal(entidade.getNomeLocal());
        dto.setDescricao(entidade.getDescricao());
        dto.setMoral(entidade.getMoral());
        dto.setDinheiro(entidade.getDinheiro());
        dto.setHorarioAbertura(entidade.getHorarioAbertura());
        dto.setHorarioFechamento(entidade.getHorarioFechamento());

        // Mapeamento de IDs
        dto.setProprietarioId(entidade.getProprietario() != null ? entidade.getProprietario().getId_personagens() : null);
        dto.setFuncionariosIds(entidade.getFuncionarios().stream()
                .map(Personagem::getId_personagens)
                .collect(Collectors.toList()));

        // Normalização das Fotos para o React
        List<String> listaFotos = new ArrayList<>();
        if (entidade.getFotos() != null && entidade.getFotos().getImageUrl() != null) {
            listaFotos.add(entidade.getFotos().getImageUrl());
        }
        dto.setFotos(listaFotos);

        return dto;
    }

    /*
     * =========================
     * CRIAR ESTABELECIMENTO
     * =========================
     */
    @Transactional
    public EstabelecimentoDTO criar(EstabelecimentoDTO dto) {
        Personagem proprietario = personagemRepository.findById(dto.getProprietarioId())
                .orElseThrow(() -> new RuntimeException("Proprietário não encontrado"));

        List<Personagem> funcionarios = (dto.getFuncionariosIds() != null)
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

        return converterParaDTO(repository.save(estabelecimento));
    }

    /*
     * =========================
     * LISTAR TODOS
     * =========================
     */
    public List<EstabelecimentoDTO> listarTodos() {
        return repository.findAll().stream()
                .map(this::converterParaDTO)
                .collect(Collectors.toList());
    }

    /*
     * =========================
     * BUSCAR POR ID
     * =========================
     */
    public EstabelecimentoDTO buscarPorIdDTO(Long id) {
        Estabelecimento ent = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Estabelecimento não encontrado"));
        return converterParaDTO(ent);
    }

    // Método interno para operações que precisam da entidade pura
    private Estabelecimento buscarEntidade(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Estabelecimento não encontrado"));
    }

    /*
     * =========================
     * ALTERAR MORAL / DINHEIRO
     * =========================
     */
    @Transactional
    public EstabelecimentoDTO alterarMoral(Long id, int quantidade) {
        Estabelecimento estabelecimento = buscarEntidade(id);
        estabelecimento.alterarMoral(quantidade);
        return converterParaDTO(repository.save(estabelecimento));
    }

    @Transactional
    public EstabelecimentoDTO alterarDinheiro(Long id, double valor) {
        Estabelecimento estabelecimento = buscarEntidade(id);
        estabelecimento.alterarDinheiro(valor);
        return converterParaDTO(repository.save(estabelecimento));
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
     * UPLOAD FOTO (SOBRESCREVE)
     * =========================
     */
    @Transactional
    public EstabelecimentoDTO adicionarFoto(Long estabelecimentoId, MultipartFile file) {
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
}