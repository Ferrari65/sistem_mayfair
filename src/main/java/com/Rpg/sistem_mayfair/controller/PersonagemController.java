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
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/personagens")
@RequiredArgsConstructor
@CrossOrigin("*") // Ajuste conforme a necessidade do seu Front-end
public class PersonagemController {

    private final PersonagemRepository personagemRepository;
    private final FamiliaRepository familiaRepository;
    private final PrestigioService prestigioService;
    private final CloudinaryService cloudinaryService;

    /**
     * Método Auxiliar para limpar a URL caso venha como JSON do Front
     */
    private String extrairUrlLimpa(String urlRaw) {
        if (urlRaw == null || urlRaw.isBlank()) return null;

        String url = urlRaw.trim();
        if (url.startsWith("{") && url.contains("\"url\"")) {
            try {
                // Tenta extrair o valor da chave "url" de uma string JSON manual
                return url.split("\"url\"\\s*:\\s*\"")[1].split("\"")[0];
            } catch (Exception e) {
                return url;
            }
        }
        return url;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public PersonagemResponseDTO criarPersonagem(@RequestBody PersonagemDTO dto) {
        Personagem personagem = new Personagem();
        personagem.setNome(dto.getName());
        personagem.setIdade(dto.getAge());
        personagem.setTitulo(dto.getTitle());
        personagem.setPrestigio(dto.getPrestige() != null ? dto.getPrestige() : 20);
        personagem.setDescricao(dto.getDescription());

        // Aplica a limpeza da URL na criação também
        personagem.setImageUrl(extrairUrlLimpa(dto.getImageUrl()));

        if (dto.getFamilyId() != null) {
            familiaRepository.findById(dto.getFamilyId())
                    .ifPresent(personagem::setFamilia);
        }

        return new PersonagemResponseDTO(personagemRepository.save(personagem));
    }

    @GetMapping
    public List<PersonagemResponseDTO> listarPersonagens() {
        return personagemRepository.findAll()
                .stream()
                .map(PersonagemResponseDTO::new)
                .toList();
    }

    @GetMapping("/{id}")
    public PersonagemResponseDTO buscarPorId(@PathVariable Long id) {
        Personagem personagem = personagemRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Personagem não encontrado"));
        return new PersonagemResponseDTO(personagem);
    }

    @PutMapping("/{id}")
    public PersonagemResponseDTO atualizarPersonagem(
            @PathVariable Long id,
            @RequestBody PersonagemDTO dto
    ) {
        Personagem personagem = personagemRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Personagem não encontrado"));

        // Atualização de campos básicos
        if (dto.getName() != null) personagem.setNome(dto.getName());
        if (dto.getAge() != null) personagem.setIdade(dto.getAge());
        if (dto.getTitle() != null) personagem.setTitulo(dto.getTitle());
        if (dto.getDescription() != null) personagem.setDescricao(dto.getDescription());
        if (dto.getPrestige() != null) personagem.setPrestigio(dto.getPrestige());

        // Proteção e limpeza da URL da imagem
        if (dto.getImageUrl() != null) {
            personagem.setImageUrl(extrairUrlLimpa(dto.getImageUrl()));
        }

        // Lógica da Família
        if (dto.getFamilyId() != null) {
            Familia familia = familiaRepository.findById(dto.getFamilyId()).orElse(null);
            personagem.setFamilia(familia);
        } else {
            personagem.setFamilia(null);
        }

        return new PersonagemResponseDTO(personagemRepository.save(personagem));
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deletarPersonagem(@PathVariable Long id) {
        personagemRepository.deleteById(id);
    }

    // --- EVENTOS E PRESTÍGIO ---

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

    // --- UPLOAD ---

    @PostMapping("/upload")
    public String uploadImagem(@RequestParam("file") MultipartFile file) {
        return cloudinaryService.uploadFile(file);
    }
}