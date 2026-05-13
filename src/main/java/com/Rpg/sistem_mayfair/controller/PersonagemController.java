package com.Rpg.sistem_mayfair.controller;

import com.Rpg.sistem_mayfair.domain.Enum.StatusCivil;
import com.Rpg.sistem_mayfair.domain.Familia;
import com.Rpg.sistem_mayfair.domain.Personagem;
import com.Rpg.sistem_mayfair.dto.EventoPrestigioDTO;
import com.Rpg.sistem_mayfair.dto.personagem.PersonagemDTO;
import com.Rpg.sistem_mayfair.dto.personagem.PersonagemResponseDTO;
import com.Rpg.sistem_mayfair.repository.FamiliaRepository;
import com.Rpg.sistem_mayfair.repository.PersonagemRepository;
import com.Rpg.sistem_mayfair.service.CloudinaryService;
import com.Rpg.sistem_mayfair.service.PersonagemService;
import com.Rpg.sistem_mayfair.service.PrestigioService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/personagens")
@RequiredArgsConstructor
public class PersonagemController {

    private final PersonagemRepository personagemRepository;
    private final PrestigioService prestigioService;
    private final CloudinaryService cloudinaryService;
    private final PersonagemService service;
    private final FamiliaRepository familiaRepository;

    // =====================
    // CREATE (ADMIN ONLY)
    // =====================
    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public PersonagemResponseDTO criarPersonagem(
            @RequestBody PersonagemDTO dto
    ) {

        Personagem salvo = service.criar(dto);

        return new PersonagemResponseDTO(
                salvo,
                true
        );
    }

    // =====================
    // LIST (PUBLIC)
    // =====================
    @GetMapping
    public List<PersonagemResponseDTO> listarPersonagens(
            Authentication auth
    ) {

        boolean isAdmin = isAdmin(auth);

        return personagemRepository.findAll()
                .stream()
                .map(personagem ->
                        new PersonagemResponseDTO(
                                personagem,
                                isAdmin
                        )
                )
                .toList();
    }

    // =====================
    // GET BY ID (PUBLIC)
    // =====================
    @GetMapping("/{id}")
    public PersonagemResponseDTO buscarPorId(
            @PathVariable Long id,
            Authentication auth
    ) {

        boolean isAdmin = isAdmin(auth);

        Personagem personagem =
                personagemRepository.findById(id)
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "Personagem não encontrado"
                                )
                        );

        return new PersonagemResponseDTO(
                personagem,
                isAdmin
        );
    }

    // =====================
    // UPDATE (ADMIN ONLY)
    // =====================
    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/{id}")
    // =========================
// ATUALIZAR (SAFE UPDATE)
// =========================
    public Personagem atualizar(Long id, PersonagemDTO dto) {

        Personagem personagem = personagemRepository
                .findById(id)
                .orElseThrow(() ->
                        new RuntimeException(
                                "Personagem não encontrado"
                        )
                );

        // =========================
        // NOME
        // =========================
        if (dto.getName() != null
                && !dto.getName().isBlank()) {

            personagem.setNome(dto.getName());
        }

        // =========================
        // IDADE
        // =========================
        if (dto.getAge() != null) {

            personagem.setIdade(dto.getAge());
        }

        // =========================
        // TITULO
        // =========================
        if (dto.getTitle() != null) {

            personagem.setTitulo(dto.getTitle());
        }

        // =========================
        // PRESTIGIO
        // =========================
        if (dto.getPrestige() != null) {

            personagem.setPrestigio(dto.getPrestige());
        }

        // =========================
        // DESCRIÇÃO
        // =========================
        if (dto.getDescription() != null) {

            personagem.setDescricao(dto.getDescription());
        }

        // =========================
        // SHAPE
        // =========================
        if (dto.getShape() != null) {

            personagem.setShape(dto.getShape());
        }

        // =========================
        // IMAGE URL
        // =========================
        if (dto.getImageUrl() != null) {

            personagem.setImageUrl(dto.getImageUrl());
        }

        // =========================
        // GENERO
        // =========================
        if (dto.getGenero() != null) {

            personagem.setGenero(dto.getGenero());
        }

        // =========================
        // STATUS CIVIL
        // =========================
        if (dto.getStatusCivil() != null) {

            personagem.setStatusCivil(dto.getStatusCivil());
        }

        // =========================
        // FAMILIA
        // =========================
        if (dto.getFamilyId() != null) {

            // REMOVE FAMILIA
            if (dto.getFamilyId() <= 0) {

                personagem.setFamilia(null);

            } else {

                Familia familia = familiaRepository
                        .findById(dto.getFamilyId())
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "Família não encontrada"
                                )
                        );

                personagem.setFamilia(familia);
            }
        }

        // =========================
        // PARCEIRO
        // =========================
        if (dto.getParceiroId() != null) {

            Personagem parceiro = personagemRepository
                    .findById(dto.getParceiroId())
                    .orElseThrow(() ->
                            new RuntimeException(
                                    "Parceiro não encontrado"
                            )
                    );

            personagem.setParceiro(parceiro);

        } else if (dto.getStatusCivil() != null
                && dto.getStatusCivil() == StatusCivil.SOLTEIRO) {

            // remove parceiro automaticamente
            personagem.setParceiro(null);
        }

        return personagemRepository.save(personagem);
    }

    // =====================
    // DELETE (ADMIN ONLY)
    // =====================
    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deletarPersonagem(
            @PathVariable Long id
    ) {

        Personagem personagem =
                personagemRepository.findById(id)
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "Personagem não encontrado"
                                )
                        );

        // =========================
        // REMOVE IMAGEM CLOUDINARY
        // =========================
        if (personagem.getImageUrl() != null
                && !personagem.getImageUrl().isBlank()) {

            cloudinaryService.deleteFile(
                    personagem.getImageUrl()
            );
        }

        personagemRepository.delete(personagem);
    }

    // =====================
    // EVENTOS (ADMIN ONLY)
    // =====================
    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/{id}/eventos")
    public PersonagemResponseDTO adicionarEvento(
            @PathVariable Long id,
            @RequestBody EventoPrestigioDTO dto
    ) {

        Personagem atualizado =
                prestigioService.aplicarEvento(
                        id,
                        dto.getReason(),
                        dto.getDelta()
                );

        return new PersonagemResponseDTO(
                atualizado,
                true
        );
    }

    // =====================
    // RECALCULAR PRESTIGIO
    // =====================
    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/{id}/recalcular-prestigio")
    public PersonagemResponseDTO recalcular(
            @PathVariable Long id
    ) {

        Personagem personagem =
                prestigioService.recalcularPrestigio(id);

        return new PersonagemResponseDTO(
                personagem,
                true
        );
    }

    // =====================
    // PLAYER
    // =====================
    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/{personagemId}/player/{playerId}")
    public ResponseEntity<Personagem> atribuirPlayer(
            @PathVariable Long personagemId,
            @PathVariable Long playerId
    ) {

        Personagem personagem =
                service.atribuirPlayer(
                        personagemId,
                        playerId
                );

        return ResponseEntity.ok(personagem);
    }

    // =====================
    // SUBSTITUIR IMAGEM
    // =====================
    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/{id}/upload")
    public ResponseEntity<PersonagemResponseDTO> substituirImagem(
            @PathVariable Long id,
            @RequestParam("file") MultipartFile file
    ) {

        // =========================
        // BUSCA PERSONAGEM
        // =========================
        Personagem personagem =
                personagemRepository.findById(id)
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "Personagem não encontrado"
                                )
                        );

        // =========================
        // REMOVE IMAGEM ANTIGA
        // =========================
        if (personagem.getImageUrl() != null
                && !personagem.getImageUrl().isBlank()) {

            cloudinaryService.deleteFile(
                    personagem.getImageUrl()
            );
        }

        // =========================
        // NOVO UPLOAD
        // =========================
        String novaUrl =
                cloudinaryService.uploadFile(file);

        // =========================
        // ATUALIZA URL
        // =========================
        personagem.setImageUrl(novaUrl);

        // =========================
        // SALVA
        // =========================
        Personagem atualizado =
                personagemRepository.save(personagem);

        return ResponseEntity.ok(
                new PersonagemResponseDTO(
                        atualizado,
                        true
                )
        );
    }

    // =====================
    // DEBUG AUTH
    // =====================
    @GetMapping("/debug-auth")
    public ResponseEntity<?> debugAuth(
            Authentication auth
    ) {

        if (auth == null) {
            return ResponseEntity.ok("AUTH NULL");
        }

        return ResponseEntity.ok(
                auth.getAuthorities()
        );
    }

    // =====================
    // UTILS
    // =====================
    private boolean isAdmin(Authentication auth) {

        if (auth == null
                || !auth.isAuthenticated()) {

            return false;
        }

        return auth.getAuthorities()
                .stream()
                .anyMatch(authority ->
                        authority.getAuthority()
                                .equals("ROLE_ADMIN")
                );
    }
}