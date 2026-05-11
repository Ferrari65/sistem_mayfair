package com.Rpg.sistem_mayfair.service.estabelecimetno;

import com.Rpg.sistem_mayfair.domain.Personagem;
import com.Rpg.sistem_mayfair.domain.estabelecimento.Estabelecimento;
import com.Rpg.sistem_mayfair.domain.estabelecimento.FotoEstabelecimento;
import com.Rpg.sistem_mayfair.dto.estabelecimento.EstabelecimentoDTO;
import com.Rpg.sistem_mayfair.dto.estabelecimento.FotoEstabelecimentoDTO;
import com.Rpg.sistem_mayfair.repository.PersonagemRepository;
import com.Rpg.sistem_mayfair.repository.estabelecimento.EstabelecimentoRepository;
import com.Rpg.sistem_mayfair.service.CloudinaryService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Service
@RequiredArgsConstructor
public class EstabelecimentoService {

    private final EstabelecimentoRepository repository;
    private final PersonagemRepository personagemRepository;
    private final CloudinaryService cloudinaryService;

    /*
     * =========================
     * CREATE
     * =========================
     */
    public EstabelecimentoDTO criar(EstabelecimentoDTO dto) {

        Personagem proprietario = null;

        if (dto.getProprietarioId() != null) {
            proprietario = personagemRepository.findById(dto.getProprietarioId())
                    .orElseThrow(() -> new RuntimeException("Proprietário não encontrado"));
        }

        List<Personagem> funcionarios =
                personagemRepository.findAllById(dto.getFuncionariosIds());

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

        return toDTO(repository.save(estabelecimento));
    }

    /*
     * =========================
     * LIST ALL (FIXED DTO)
     * =========================
     */
    public List<EstabelecimentoDTO> listarTodos() {
        return repository.findAll()
                .stream()
                .map(this::toDTO)
                .toList();
    }

    /*
     * =========================
     * FIND BY ID (FIXED DTO)
     * =========================
     */
    public EstabelecimentoDTO buscarPorId(Long id) {
        Estabelecimento est = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Estabelecimento não encontrado"));

        return toDTO(est);
    }

    /*
     * =========================
     * MORAL
     * =========================
     */
    public EstabelecimentoDTO alterarMoral(Long id, int quantidade) {
        Estabelecimento est = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Estabelecimento não encontrado"));

        est.alterarMoral(quantidade);

        return toDTO(repository.save(est));
    }

    /*
     * =========================
     * DINHEIRO
     * =========================
     */
    public EstabelecimentoDTO alterarDinheiro(Long id, double valor) {
        Estabelecimento est = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Estabelecimento não encontrado"));

        est.alterarDinheiro(valor);

        return toDTO(repository.save(est));
    }

    /*
     * =========================
     * DELETE
     * =========================
     */
    public void deletar(Long id) {
        Estabelecimento est = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Estabelecimento não encontrado"));

        repository.delete(est);
    }

    /*
     * =========================
     * FOTO UPLOAD
     * =========================
     */
    public EstabelecimentoDTO adicionarFoto(Long id, MultipartFile file) {

        Estabelecimento est = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Estabelecimento não encontrado"));

        String imageUrl = cloudinaryService.uploadFile(file);

        FotoEstabelecimento foto = est.getFoto();

        if (foto == null) {
            foto = new FotoEstabelecimento();
            foto.setEstabelecimento(est);
            est.setFoto(foto);
        }

        foto.setImageUrl(imageUrl);

        return toDTO(repository.save(est));
    }

    /*
     * =========================
     * MAPPER ENTITY → DTO
     * =========================
     */
    private EstabelecimentoDTO toDTO(Estabelecimento est) {

        EstabelecimentoDTO dto = new EstabelecimentoDTO();

        dto.setNomeLocal(est.getNomeLocal());
        dto.setDescricao(est.getDescricao());
        dto.setMoral(est.getMoral());
        dto.setDinheiro(est.getDinheiro());
        dto.setHorarioAbertura(est.getHorarioAbertura());
        dto.setHorarioFechamento(est.getHorarioFechamento());

        dto.setProprietarioId(
                est.getProprietario() != null ? est.getProprietario().getId_personagens() : null
        );

        dto.setFuncionariosIds(
                est.getFuncionarios().stream()
                        .map(Personagem::getId_personagens)
                        .toList()
        );

        // 🔥 FOTO FIX AQUI
        if (est.getFoto() != null) {
            FotoEstabelecimentoDTO fotoDTO = new FotoEstabelecimentoDTO();
            fotoDTO.setImageUrl(est.getFoto().getImageUrl());
            dto.setFoto(fotoDTO);
        }

        return dto;
    }
}