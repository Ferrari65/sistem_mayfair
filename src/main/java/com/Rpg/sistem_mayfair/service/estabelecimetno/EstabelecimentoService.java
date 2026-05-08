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
     * CRIAR ESTABELECIMENTO
     * =========================
     */
    public Estabelecimento criar(EstabelecimentoDTO dto) {

        Personagem proprietario =
                personagemRepository.findById(dto.getProprietarioId())
                        .orElseThrow(() ->
                                new RuntimeException("Proprietário não encontrado"));

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

        return repository.save(estabelecimento);
    }

    /*
     * =========================
     * LISTAR TODOS (CORRIGIDO)
     * =========================
     */
    public List<Estabelecimento> listarTodos() {
        return repository.findAll();
    }

    /*
     * =========================
     * BUSCAR POR ID
     * =========================
     */
    public Estabelecimento buscarPorId(Long id) {
        return repository.findById(id)
                .orElseThrow(() ->
                        new RuntimeException("Estabelecimento não encontrado"));
    }

    /*
     * =========================
     * ALTERAR MORAL
     * =========================
     */
    public Estabelecimento alterarMoral(Long id, int quantidade) {
        Estabelecimento estabelecimento = buscarPorId(id);
        estabelecimento.alterarMoral(quantidade);
        return repository.save(estabelecimento);
    }

    /*
     * =========================
     * ALTERAR DINHEIRO
     * =========================
     */
    public Estabelecimento alterarDinheiro(Long id, double valor) {
        Estabelecimento estabelecimento = buscarPorId(id);
        estabelecimento.alterarDinheiro(valor);
        return repository.save(estabelecimento);
    }

    /*
     * =========================
     * DELETAR
     * =========================
     */
    public void deletar(Long id) {
        Estabelecimento estabelecimento = buscarPorId(id);
        repository.delete(estabelecimento);
    }

    /*
     * =========================
     * UPLOAD FOTO (SOBRESCREVE)
     * =========================
     */
    public FotoEstabelecimento adicionarFoto(Long estabelecimentoId, MultipartFile file) {

        Estabelecimento estabelecimento = buscarPorId(estabelecimentoId);

        String imageUrl = cloudinaryService.uploadFile(file);

        FotoEstabelecimento foto = estabelecimento.getFoto();

        if (foto == null) {
            foto = new FotoEstabelecimento();
            foto.setEstabelecimento(estabelecimento);
        }

        foto.setImageUrl(imageUrl);

        estabelecimento.setFoto(foto);

        repository.save(estabelecimento);

        return foto;
    }
}