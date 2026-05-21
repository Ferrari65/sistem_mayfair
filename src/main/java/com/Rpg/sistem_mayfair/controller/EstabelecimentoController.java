package com.Rpg.sistem_mayfair.controller;

import com.Rpg.sistem_mayfair.domain.estabelecimento.AmbienteEstabelecimento;
import com.Rpg.sistem_mayfair.dto.estabelecimento.*;
import com.Rpg.sistem_mayfair.service.estabelecimetno.EstabelecimentoService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/estabelecimentos")
@RequiredArgsConstructor
public class EstabelecimentoController {

    private final EstabelecimentoService service;

    /*
     * =========================
     * ESTABELECIMENTO - CRUD
     * =========================
     */

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping
    public ResponseEntity<EstabelecimentoDTO> criar(
            @RequestBody @Valid EstabelecimentoDTO dto
    ) {
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(service.criar(dto));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/{id}")
    public ResponseEntity<EstabelecimentoDTO> atualizar(
            @PathVariable Long id,
            @RequestBody @Valid EstabelecimentoDTO dto
    ) {
        return ResponseEntity.ok(service.atualizar(id, dto));
    }

    @GetMapping
    public ResponseEntity<List<EstabelecimentoDTO>> listarTodos() {
        return ResponseEntity.ok(service.listarTodos());
    }

    @GetMapping("/{id}")
    public ResponseEntity<EstabelecimentoDTO> buscarPorId(
            @PathVariable Long id
    ) {
        return ResponseEntity.ok(service.buscarPorIdDTO(id));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(
            @PathVariable Long id
    ) {
        service.deletar(id);
        return ResponseEntity.noContent().build();
    }

    /*
     * =========================
     * MORAL / DINHEIRO
     * =========================
     */

    @PreAuthorize("hasRole('ADMIN')")
    @PatchMapping("/{id}/moral")
    public ResponseEntity<EstabelecimentoDTO> alterarMoral(
            @PathVariable Long id,
            @RequestParam int quantidade
    ) {
        return ResponseEntity.ok(service.alterarMoral(id, quantidade));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PatchMapping("/{id}/dinheiro")
    public ResponseEntity<EstabelecimentoDTO> alterarDinheiro(
            @PathVariable Long id,
            @RequestParam double valor
    ) {
        return ResponseEntity.ok(service.alterarDinheiro(id, valor));
    }

    /*
     * =========================
     * MOVIMENTAÇÕES
     * =========================
     */

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/{id}/movimentacoes")
    public ResponseEntity<Void> registrarMovimentacao(
            @PathVariable Long id,
            @RequestBody @Valid RegistrarMovimentacaoDTO dto
    ) {
        service.registrarMovimentacao(id, dto);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .build();
    }

    @GetMapping("/{id}/estatisticas")
    public ResponseEntity<EstatisticasEstabelecimentoDTO> buscarEstatisticas(
            @PathVariable Long id
    ) {
        return ResponseEntity.ok(service.buscarEstatisticas(id));
    }

    /*
     * =========================
     * FOTOS
     * =========================
     */

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/{id}/fotos")
    public ResponseEntity<EstabelecimentoDTO> adicionarFoto(
            @PathVariable Long id,
            @RequestParam("file") MultipartFile file
    ) {
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(service.adicionarFoto(id, file));
    }

    /*
     * =========================
     * AMBIENTES
     * =========================
     */

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/{id}/ambientes")
    public ResponseEntity<AmbienteEstabelecimentoDTO> criarAmbiente(
            @PathVariable Long id,
            @RequestBody AmbienteEstabelecimento ambiente
    ) {
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(service.criarAmbiente(id, ambiente));
    }

    @GetMapping("/{id}/ambientes")
    public ResponseEntity<List<AmbienteEstabelecimentoDTO>> listarAmbientes(
            @PathVariable Long id
    ) {
        return ResponseEntity.ok(service.listarAmbientes(id));
    }

    @GetMapping("/ambientes/{ambienteId}")
    public ResponseEntity<AmbienteEstabelecimentoDTO> buscarAmbiente(
            @PathVariable Long ambienteId
    ) {
        return ResponseEntity.ok(service.buscarAmbiente(ambienteId));
    }

    @PutMapping("/ambientes/{ambienteId}")
    public ResponseEntity<AmbienteEstabelecimentoDTO> atualizarAmbiente(
            @PathVariable Long ambienteId,
            @RequestBody AmbienteEstabelecimento dto
    ) {
        return ResponseEntity.ok(service.atualizarAmbiente(ambienteId, dto));
    }

    @DeleteMapping("/ambientes/{ambienteId}")
    public ResponseEntity<Void> deletarAmbiente(
            @PathVariable Long ambienteId
    ) {
        service.deletarAmbiente(ambienteId);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/ambientes/{id}/fotos")
    public ResponseEntity<AmbienteEstabelecimentoDTO> adicionarFotoAmbiente(
            @PathVariable Long id,
            @RequestParam MultipartFile file
    ) {
        return ResponseEntity.ok(
                service.adicionarFotoAmbiente(id, file)
        );
    }
}