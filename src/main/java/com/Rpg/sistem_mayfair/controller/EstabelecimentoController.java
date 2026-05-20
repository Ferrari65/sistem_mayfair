package com.Rpg.sistem_mayfair.controller;

import com.Rpg.sistem_mayfair.dto.estabelecimento.EstabelecimentoDTO;
import com.Rpg.sistem_mayfair.dto.estabelecimento.EstatisticasEstabelecimentoDTO;
import com.Rpg.sistem_mayfair.dto.estabelecimento.RegistrarMovimentacaoDTO;
import com.Rpg.sistem_mayfair.service.estabelecimetno.EstabelecimentoService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
     * CRIAR
     * =========================
     */
    @PostMapping
    public ResponseEntity<EstabelecimentoDTO> criar(
            @RequestBody @Valid EstabelecimentoDTO dto
    ) {

        EstabelecimentoDTO novoEstabelecimento = service.criar(dto);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(novoEstabelecimento);
    }

    /*
     * =========================
     * ATUALIZAR
     * =========================
     */
    @PutMapping("/{id}")
    public ResponseEntity<EstabelecimentoDTO> atualizar(
            @PathVariable Long id,
            @RequestBody @Valid EstabelecimentoDTO dto
    ) {

        EstabelecimentoDTO atualizado = service.atualizar(id, dto);

        return ResponseEntity.ok(atualizado);
    }

    /*
     * =========================
     * LISTAR TODOS
     * =========================
     */
    @GetMapping
    public ResponseEntity<List<EstabelecimentoDTO>> listarTodos() {

        return ResponseEntity.ok(service.listarTodos());
    }

    /*
     * =========================
     * BUSCAR POR ID
     * =========================
     */
    @GetMapping("/{id}")
    public ResponseEntity<EstabelecimentoDTO> buscarPorId(
            @PathVariable Long id
    ) {

        return ResponseEntity.ok(service.buscarPorIdDTO(id));
    }

    /*
     * =========================
     * ALTERAR MORAL
     * =========================
     */
    @PatchMapping("/{id}/moral")
    public ResponseEntity<EstabelecimentoDTO> alterarMoral(
            @PathVariable Long id,
            @RequestParam int quantidade
    ) {

        return ResponseEntity.ok(
                service.alterarMoral(id, quantidade)
        );
    }

    /*
     * =========================
     * ALTERAR DINHEIRO
     * =========================
     */
    @PatchMapping("/{id}/dinheiro")
    public ResponseEntity<EstabelecimentoDTO> alterarDinheiro(
            @PathVariable Long id,
            @RequestParam double valor
    ) {

        return ResponseEntity.ok(
                service.alterarDinheiro(id, valor)
        );
    }

    /*
     * =========================
     * REGISTRAR MOVIMENTAÇÃO
     * =========================
     */
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

    /*
     * =========================
     * DELETAR
     * =========================
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(
            @PathVariable Long id
    ) {

        service.deletar(id);

        return ResponseEntity.noContent().build();
    }

    /*
     * =========================
     * ADICIONAR FOTO
     * =========================
     */
    @PostMapping("/{id}/fotos")
    public ResponseEntity<EstabelecimentoDTO> adicionarFoto(
            @PathVariable Long id,
            @RequestParam("file") MultipartFile file
    ) {

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(service.adicionarFoto(id, file));
    }

    @GetMapping("/{id}/estatisticas")
    public ResponseEntity<EstatisticasEstabelecimentoDTO> buscarEstatisticas(
            @PathVariable Long id
    ) {

        return ResponseEntity.ok(
                service.buscarEstatisticas(id)
        );
    }
}