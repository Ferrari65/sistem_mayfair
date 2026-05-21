package com.Rpg.sistem_mayfair.service.estabelecimetno;

import com.Rpg.sistem_mayfair.domain.Enum.TipoMovimentacaoEstabelecimento;
import com.Rpg.sistem_mayfair.domain.Personagem;
import com.Rpg.sistem_mayfair.domain.estabelecimento.*;
import com.Rpg.sistem_mayfair.dto.estabelecimento.*;
import com.Rpg.sistem_mayfair.repository.PersonagemRepository;
import com.Rpg.sistem_mayfair.repository.estabelecimento.AmbienteEstabelecimentoRepository;
import com.Rpg.sistem_mayfair.repository.estabelecimento.EstabelecimentoRepository;
import com.Rpg.sistem_mayfair.repository.estabelecimento.MovimentacaoEstabelecimentoRepository;
import com.Rpg.sistem_mayfair.service.CloudinaryService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class EstabelecimentoService {

    private final EstabelecimentoRepository estabelecimentoRepository;
    private final PersonagemRepository personagemRepository;
    private final CloudinaryService cloudinaryService;
    private final MovimentacaoEstabelecimentoRepository movimentacaoRepository;
    private final AmbienteEstabelecimentoRepository ambienteRepository;

    /*
     * =========================
     * MAPPERS
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

        dto.setProprietarioId(
                entidade.getProprietario() != null
                        ? entidade.getProprietario().getId_personagens()
                        : null
        );

        dto.setFuncionariosIds(
                entidade.getFuncionarios()
                        .stream()
                        .map(Personagem::getId_personagens)
                        .collect(Collectors.toList())
        );

        /*
         * FOTOS
         */
        List<FotoEstabelecimentoDTO> fotosDTO = new ArrayList<>();

        if (entidade.getFotos() != null) {
            fotosDTO = entidade.getFotos()
                    .stream()
                    .map(foto -> {
                        FotoEstabelecimentoDTO dtoFoto = new FotoEstabelecimentoDTO();
                        dtoFoto.setId(foto.getId());
                        dtoFoto.setImageUrl(foto.getImageUrl());
                        dtoFoto.setPrincipal(foto.getPrincipal());
                        return dtoFoto;
                    })
                    .collect(Collectors.toList());
        }

        dto.setFotos(fotosDTO);

        /*
         * AMBIENTES
         */
        List<AmbienteEstabelecimentoDTO> ambientesDTO =
                ambienteRepository.findByEstabelecimentoId(entidade.getId())
                        .stream()
                        .map(this::mapAmbienteToDTO)
                        .collect(Collectors.toList());

        dto.setAmbientes(ambientesDTO);

        return dto;
    }

    private AmbienteEstabelecimentoDTO mapAmbienteToDTO(AmbienteEstabelecimento amb) {

        AmbienteEstabelecimentoDTO dto = new AmbienteEstabelecimentoDTO();

        dto.setId(amb.getId());
        dto.setNome(amb.getNome());
        dto.setDescricao(amb.getDescricao());
        dto.setTipo(amb.getTipo() != null ? amb.getTipo().name() : null);

        return dto;
    }

    private Estabelecimento buscarEntidade(Long id) {
        return estabelecimentoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Estabelecimento não encontrado"));
    }

    private AmbienteEstabelecimento buscarAmbienteEntity(Long id) {
        return ambienteRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Ambiente não encontrado"));
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

        estabelecimentoRepository.save(estabelecimento);

        return converterParaDTO(estabelecimento);
    }

    /*
     * =========================
     * ATUALIZAR ESTABELECIMENTO
     * =========================
     */

    @Transactional
    public EstabelecimentoDTO atualizar(Long id, EstabelecimentoDTO dto) {

        Estabelecimento estabelecimento = buscarEntidade(id);

        Personagem proprietario = personagemRepository.findById(dto.getProprietarioId())
                .orElseThrow(() -> new RuntimeException("Proprietário não encontrado"));

        List<Personagem> funcionarios =
                dto.getFuncionariosIds() != null
                        ? personagemRepository.findAllById(dto.getFuncionariosIds())
                        : new ArrayList<>();

        estabelecimento.setNomeLocal(dto.getNomeLocal());
        estabelecimento.setDescricao(dto.getDescricao());
        estabelecimento.setHorarioAbertura(dto.getHorarioAbertura());
        estabelecimento.setHorarioFechamento(dto.getHorarioFechamento());
        estabelecimento.setMoral(dto.getMoral() != null ? dto.getMoral() : estabelecimento.getMoral());
        estabelecimento.setDinheiro(dto.getDinheiro() != null ? dto.getDinheiro() : estabelecimento.getDinheiro());
        estabelecimento.setProprietario(proprietario);
        estabelecimento.setFuncionarios(funcionarios);

        estabelecimentoRepository.save(estabelecimento);

        return converterParaDTO(estabelecimento);
    }

    /*
     * =========================
     * LISTAR / BUSCAR
     * =========================
     */

    public List<EstabelecimentoDTO> listarTodos() {
        return estabelecimentoRepository.findAll()
                .stream()
                .map(this::converterParaDTO)
                .collect(Collectors.toList());
    }

    public EstabelecimentoDTO buscarPorIdDTO(Long id) {
        return converterParaDTO(buscarEntidade(id));
    }

    /*
     * =========================
     * MORAL / DINHEIRO
     * =========================
     */

    @Transactional
    public EstabelecimentoDTO alterarMoral(Long id, int quantidade) {

        Estabelecimento estabelecimento = buscarEntidade(id);
        estabelecimento.alterarMoral(quantidade);

        estabelecimentoRepository.save(estabelecimento);
        return converterParaDTO(estabelecimento);
    }

    @Transactional
    public EstabelecimentoDTO alterarDinheiro(Long id, double valor) {

        Estabelecimento estabelecimento = buscarEntidade(id);
        estabelecimento.alterarDinheiro(valor);

        estabelecimentoRepository.save(estabelecimento);
        return converterParaDTO(estabelecimento);
    }

    /*
     * =========================
     * FOTO
     * =========================
     */

    @Transactional
    public EstabelecimentoDTO adicionarFoto(Long estabelecimentoId, MultipartFile file) {

        Estabelecimento estabelecimento = buscarEntidade(estabelecimentoId);

        String url = cloudinaryService.uploadFile(file);

        FotoEstabelecimento foto = new FotoEstabelecimento();
        foto.setImageUrl(url);
        foto.setPrincipal(false);
        foto.setEstabelecimento(estabelecimento);

        estabelecimento.getFotos().add(foto);

        estabelecimentoRepository.save(estabelecimento);

        return converterParaDTO(estabelecimento);
    }

    /*
     * =========================
     * AMBIENTE
     * =========================
     */

    @Transactional
    public AmbienteEstabelecimentoDTO criarAmbiente(Long estabelecimentoId, AmbienteEstabelecimento ambiente) {

        Estabelecimento estabelecimento = buscarEntidade(estabelecimentoId);

        ambiente.setEstabelecimento(estabelecimento);

        AmbienteEstabelecimento salvo = ambienteRepository.save(ambiente);

        return mapAmbienteToDTO(salvo);
    }

    public List<AmbienteEstabelecimentoDTO> listarAmbientes(Long estabelecimentoId) {
        return ambienteRepository.findByEstabelecimentoId(estabelecimentoId)
                .stream()
                .map(this::mapAmbienteToDTO)
                .collect(Collectors.toList());
    }

    public AmbienteEstabelecimentoDTO buscarAmbiente(Long id) {
        return mapAmbienteToDTO(buscarAmbienteEntity(id));
    }

    @Transactional
    public AmbienteEstabelecimentoDTO atualizarAmbiente(Long id, AmbienteEstabelecimento dto) {

        AmbienteEstabelecimento ambiente = buscarAmbienteEntity(id);

        ambiente.setNome(dto.getNome());
        ambiente.setDescricao(dto.getDescricao());
        ambiente.setTipo(dto.getTipo());

        return mapAmbienteToDTO(ambienteRepository.save(ambiente));
    }

    @Transactional
    public void deletarAmbiente(Long id) {
        ambienteRepository.deleteById(id);
    }

    /*
     * =========================
     * MOVIMENTAÇÃO / ESTATÍSTICAS
     * =========================
     */

    @Transactional
    public void registrarMovimentacao(Long estabelecimentoId, RegistrarMovimentacaoDTO dto) {

        Estabelecimento estabelecimento = estabelecimentoRepository.findById(estabelecimentoId)
                .orElseThrow(() -> new EntityNotFoundException("Estabelecimento não encontrado."));

        int impactoMoral = dto.getImpactoMoral();

        if (impactoMoral > 100 || impactoMoral < -100) {
            throw new IllegalArgumentException("Impacto moral inválido. Limite: -100 até 100.");
        }

        MovimentacaoEstabelecimento movimentacao =
                MovimentacaoEstabelecimento.builder()
                        .estabelecimento(estabelecimento)
                        .tipo(dto.getTipo())
                        .impactoMoral(impactoMoral)
                        .observacao(dto.getObservacao())
                        .build();

        movimentacaoRepository.save(movimentacao);

        estabelecimento.alterarMoral(impactoMoral);
        estabelecimento.registrarMovimentacao();

        estabelecimentoRepository.save(estabelecimento);
    }

    @Transactional
    public void deletar(Long id) {
        Estabelecimento estabelecimento = buscarEntidade(id);
        estabelecimentoRepository.delete(estabelecimento);
    }

    public EstatisticasEstabelecimentoDTO buscarEstatisticas(Long estabelecimentoId) {

        Estabelecimento estabelecimento = buscarEntidade(estabelecimentoId);

        List<Object[]> resultado =
                movimentacaoRepository.contarMovimentacoesPorTipo(estabelecimentoId);

        Map<String, Long> movimentacoesPorTipo = new HashMap<>();

        for (Object[] linha : resultado) {

            TipoMovimentacaoEstabelecimento tipo =
                    (TipoMovimentacaoEstabelecimento) linha[0];

            Long quantidade = (Long) linha[1];

            movimentacoesPorTipo.put(tipo.name(), quantidade);
        }

        Integer impactoMoralTotal =
                movimentacaoRepository.somarImpactoMoral(estabelecimentoId);

        MovimentacaoEstabelecimento ultima =
                movimentacaoRepository.findTopByEstabelecimentoIdOrderByDataMovimentacaoDesc(estabelecimentoId);

        return new EstatisticasEstabelecimentoDTO(
                estabelecimento.getTotalMovimentacoes(),
                movimentacoesPorTipo,
                impactoMoralTotal,
                ultima != null ? ultima.getDataMovimentacao() : null
        );
    }
}