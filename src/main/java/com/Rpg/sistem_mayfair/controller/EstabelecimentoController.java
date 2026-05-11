package com.Rpg.sistem_mayfair.controller;

import com.Rpg.sistem_mayfair.dto.estabelecimento.EstabelecimentoDTO;
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
     * CRIAR
     * Retorna EstabelecimentoDTO (já com a estrutura de fotos correta)
     */
    @PostMapping
    public ResponseEntity<EstabelecimentoDTO> criar(@RequestBody @Valid EstabelecimentoDTO dto) {
        EstabelecimentoDTO novoEstabelecimento = service.criar(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(novoEstabelecimento);
    }

    /*
     * LISTAR TODOS
     * Retorna List<EstabelecimentoDTO>
     */
    @GetMapping
    public ResponseEntity<List<EstabelecimentoDTO>> listarTodos() {
        return ResponseEntity.ok(service.listarTodos());
    }

    /*
     * BUSCAR POR ID
     */
    @GetMapping("/{id}")
    public ResponseEntity<EstabelecimentoDTO> buscarPorId(@PathVariable Long id) {
        return ResponseEntity.ok(service.buscarPorIdDTO(id));
    }

    /*
     * ALTERAR MORAL
     */
    @PatchMapping("/{id}/moral")
    public ResponseEntity<EstabelecimentoDTO> alterarMoral(
            @PathVariable Long id,
            @RequestParam int quantidade
    ) {
        return ResponseEntity.ok(service.alterarMoral(id, quantidade));
    }

    /*
     * ALTERAR DINHEIRO
     */
    @PatchMapping("/{id}/dinheiro")
    public ResponseEntity<EstabelecimentoDTO> alterarDinheiro(
            @PathVariable Long id,
            @RequestParam double valor
    ) {
        return ResponseEntity.ok(service.alterarDinheiro(id, valor));
    }

    /*
     * DELETAR
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Long id) {
        service.deletar(id);
        return ResponseEntity.noContent().build();
    }

    /*
     * ADICIONAR FOTO
     * Agora retorna o DTO completo, assim o Front já atualiza a imagem na hora
     */
    @PostMapping("/{id}/fotos")
    public ResponseEntity<EstabelecimentoDTO> adicionarFoto(
            @PathVariable Long id,
            @RequestParam("file") MultipartFile file
    ) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.adicionarFoto(id, file));
    }
}