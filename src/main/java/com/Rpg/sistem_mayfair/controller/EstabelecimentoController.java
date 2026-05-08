package com.Rpg.sistem_mayfair.controller;

import com.Rpg.sistem_mayfair.domain.estabelecimento.Estabelecimento;
import com.Rpg.sistem_mayfair.domain.estabelecimento.FotoEstabelecimento;
import com.Rpg.sistem_mayfair.dto.estabelecimento.EstabelecimentoDTO;
import com.Rpg.sistem_mayfair.service.estabelecimetno.EstabelecimentoService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
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
    public ResponseEntity<Estabelecimento> criar(@RequestBody @Valid EstabelecimentoDTO dto) {
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(service.criar(dto));
    }

    /*
     * =========================
     * LISTAR TODOS
     * =========================
     */
    @GetMapping
    public ResponseEntity<List<Estabelecimento>> listarTodos() {
        return ResponseEntity.ok(service.listarTodos());
    }

    /*
     * =========================
     * BUSCAR POR ID
     * =========================
     */
    @GetMapping("/{id}")
    public ResponseEntity<Estabelecimento> buscarPorId(@PathVariable Long id) {
        return ResponseEntity.ok(service.buscarPorId(id));
    }

    /*
     * =========================
     * ALTERAR MORAL
     * =========================
     */
    @PatchMapping("/{id}/moral")
    public ResponseEntity<Estabelecimento> alterarMoral(
            @PathVariable Long id,
            @RequestParam int quantidade
    ) {
        return ResponseEntity.ok(service.alterarMoral(id, quantidade));
    }

    /*
     * =========================
     * ALTERAR DINHEIRO
     * =========================
     */
    @PatchMapping("/{id}/dinheiro")
    public ResponseEntity<Estabelecimento> alterarDinheiro(
            @PathVariable Long id,
            @RequestParam double valor
    ) {
        return ResponseEntity.ok(service.alterarDinheiro(id, valor));
    }

    /*
     * =========================
     * DELETAR
     * =========================
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Long id) {
        service.deletar(id);
        return ResponseEntity.noContent().build();
    }

    /*
     * =========================
     * UPLOAD DE FOTO (CORRIGIDO)
     * =========================
     *
     * 🔥 IMPORTANTE:
     * precisa do consumes = MULTIPART_FORM_DATA_VALUE
     */
    @PostMapping(
            value = "/{id}/fotos",
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE
    )
    public ResponseEntity<FotoEstabelecimento> adicionarFoto(
            @PathVariable Long id,
            @RequestParam("file") MultipartFile file
    ) {
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(service.adicionarFoto(id, file));
    }
}