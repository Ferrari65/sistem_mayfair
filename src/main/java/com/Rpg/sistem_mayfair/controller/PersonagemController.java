package com.Rpg.sistem_mayfair.controller;

import com.Rpg.sistem_mayfair.domain.Familia;
import com.Rpg.sistem_mayfair.domain.Personagem;
import com.Rpg.sistem_mayfair.dto.EventoPrestigioDTO;
import com.Rpg.sistem_mayfair.dto.PersonagemDTO;
import com.Rpg.sistem_mayfair.dto.PersonagemResponseDTO;
import com.Rpg.sistem_mayfair.repository.FamiliaRepository;
import com.Rpg.sistem_mayfair.repository.PersonagemRepository;
import com.Rpg.sistem_mayfair.service.CloudinaryService;
import com.Rpg.sistem_mayfair.service.PrestigioService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/personagens")
@RequiredArgsConstructor
public class PersonagemController {

    private final PersonagemRepository personagemRepository;
    private final FamiliaRepository familiaRepository;
    private final PrestigioService prestigioService;
    private final CloudinaryService cloudinaryService;

    // Método Auxiliar para limpar a URL caso venha como JSON do Front
    private String extrairUrlLimpa(String urlRaw) {
        if (urlRaw == null || urlRaw.isBlank()) return null;

        String url = urlRaw.trim();
        // Se a string começar com { e contiver "url", extraímos o conteúdo entre as aspas do valor
        if (url.startsWith("{") && url.contains("\"url\"")) {
            try {
                return url.split("\"url\"\\s*:\\s*\"")[1].split("\"")[0];
            } catch (Exception e) {
                return url; // Retorna o original caso o parse falhe
            }
        }
        return url;
    }

    // =========================================
    // CRIAR PERSONAGEM (CORRIGIDO)
    // =========================================
    @PostMapping
    public PersonagemResponseDTO criarPersonagem(@RequestBody PersonagemDTO dto) {

        Familia familia = null;

        if (dto.getFamily() != null && !dto.getFamily().isBlank()) {
            familia = familiaRepository.findByNome(dto.getFamily())
                    .orElse(null);
        }

        Personagem personagem = new Personagem();
        personagem.setNome(dto.getName());
        personagem.setIdade(dto.getAge());
        personagem.setTitulo(dto.getTitle());
        personagem.setPrestigio(dto.getPrestige() != null ? dto.getPrestige() : 20);
        personagem.setDescricao(dto.getDescription());

        // 🔥 Aplica a limpeza na criação também
        personagem.setImageUrl(extrairUrlLimpa(dto.getImageUrl()));

        personagem.setFamilia(familia);

        return new PersonagemResponseDTO(
                personagemRepository.save(personagem)
        );
    }

    // =========================================
    // LISTAR
    // =========================================
    @GetMapping
    public List<PersonagemResponseDTO> listarPersonagens() {
        return personagemRepository.findAll()
                .stream()
                .map(PersonagemResponseDTO::new)
                .toList();
    }

    // =========================================
    // BUSCAR POR ID
    // =========================================
    @GetMapping("/{id}")
    public PersonagemResponseDTO buscarPorId(@PathVariable Long id) {
        Personagem personagem = personagemRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Personagem não encontrado"));
        return new PersonagemResponseDTO(personagem);
    }

    // =========================================
    // ATUALIZAR (CORRIGIDO E BLINDADO)
    // =========================================
    @PutMapping("/{id}")
    public PersonagemResponseDTO atualizarPersonagem(
            @PathVariable Long id,
            @RequestBody PersonagemDTO dto
    ) {

        Personagem personagem = personagemRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Personagem não encontrado"));

        Familia familia = null;

        if (dto.getFamily() != null && !dto.getFamily().isBlank()) {
            familia = familiaRepository.findByNome(dto.getFamily())
                    .orElse(null);
        }

        if (dto.getName() != null) personagem.setNome(dto.getName());
        if (dto.getAge() != null) personagem.setIdade(dto.getAge());
        if (dto.getTitle() != null) personagem.setTitulo(dto.getTitle());
        if (dto.getPrestige() != null) personagem.setPrestigio(dto.getPrestige());
        if (dto.getDescription() != null) personagem.setDescricao(dto.getDescription());

        // 🔥 O PONTO CHAVE: Limpa a URL antes de salvar no objeto
        if (dto.getImageUrl() != null && !dto.getImageUrl().isBlank()) {
            personagem.setImageUrl(extrairUrlLimpa(dto.getImageUrl()));
        }

        if (familia != null) {
            personagem.setFamilia(familia);
        }

        return new PersonagemResponseDTO(
                personagemRepository.save(personagem)
        );
    }

    // =========================================
    // DELETAR
    // =========================================
    @DeleteMapping("/{id}")
    public void deletarPersonagem(@PathVariable Long id) {
        personagemRepository.deleteById(id);
    }

    // =========================================
    // EVENTOS
    // =========================================
    @PostMapping("/{id}/eventos")
    public PersonagemResponseDTO adicionarEvento(
            @PathVariable Long id,
            @RequestBody EventoPrestigioDTO dto
    ) {
        Personagem atualizado = prestigioService.aplicarEvento(
                id,
                dto.getReason(),
                dto.getDelta()
        );
        return new PersonagemResponseDTO(atualizado);
    }

    @PostMapping("/{id}/recalcular-prestigio")
    public PersonagemResponseDTO recalcular(@PathVariable Long id) {
        Personagem personagem = prestigioService.recalcularPrestigio(id);
        return new PersonagemResponseDTO(personagem);
    }

    @PostMapping("/upload")
    public String uploadImagem(@RequestParam("file") MultipartFile file) {
        // Retorna a URL pura vinda do Cloudinary
        return cloudinaryService.uploadFile(file);
    }
}