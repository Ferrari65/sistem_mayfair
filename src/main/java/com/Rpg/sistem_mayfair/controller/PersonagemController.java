package com.Rpg.sistem_mayfair.controller;

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
    private final FamiliaRepository familiaRepository;
    private final PrestigioService prestigioService;
    private final CloudinaryService cloudinaryService;
    private final PersonagemService service;

    // =====================
    // CREATE (ADMIN ONLY)
    // =====================
    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public PersonagemResponseDTO criarPersonagem(
            @RequestBody PersonagemDTO dto
    ) {

        Personagem personagem = new Personagem();

        personagem.setNome(dto.getName());

        personagem.setIdade(dto.getAge());

        personagem.setTitulo(dto.getTitle());

        // =========================
        // DESCRIÇÃO
        // =========================
        personagem.setDescricao(
                dto.getDescription()
        );

        // =========================
        // SHAPE
        // =========================
        personagem.setShape(
                dto.getShape()
        );

        personagem.setPrestigio(
                dto.getPrestige() != null
                        ? dto.getPrestige()
                        : 20
        );

        personagem.setImageUrl(
                extrairUrlLimpa(
                        dto.getImageUrl()
                )
        );

        // =========================
        // FAMILY
        // =========================
        if (dto.getFamilyId() != null
                && dto.getFamilyId() > 0) {

            Familia familia = familiaRepository
                    .findById(dto.getFamilyId())
                    .orElseThrow(() ->
                            new RuntimeException(
                                    "Família não encontrada: "
                                            + dto.getFamilyId()
                            )
                    );

            personagem.setFamilia(familia);

        } else {

            personagem.setFamilia(null);
        }

        Personagem salvo =
                personagemRepository.save(personagem);

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
    public PersonagemResponseDTO atualizarPersonagem(
            @PathVariable Long id,
            @RequestBody PersonagemDTO dto
    ) {

        Personagem personagem =
                personagemRepository.findById(id)
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "Personagem não encontrado"
                                )
                        );

        if (dto.getName() != null) {
            personagem.setNome(dto.getName());
        }

        if (dto.getAge() != null) {
            personagem.setIdade(dto.getAge());
        }

        if (dto.getTitle() != null) {
            personagem.setTitulo(dto.getTitle());
        }

        // =========================
        // DESCRIÇÃO
        // =========================
        if (dto.getDescription() != null) {
            personagem.setDescricao(
                    dto.getDescription()
            );
        }

        // =========================
        // SHAPE
        // =========================
        if (dto.getShape() != null) {
            personagem.setShape(
                    dto.getShape()
            );
        }

        if (dto.getPrestige() != null) {
            personagem.setPrestigio(
                    dto.getPrestige()
            );
        }

        if (dto.getImageUrl() != null) {
            personagem.setImageUrl(
                    extrairUrlLimpa(
                            dto.getImageUrl()
                    )
            );
        }

        // =========================
        // FAMILY
        // =========================
        if (dto.getFamilyId() != null
                && dto.getFamilyId() > 0) {

            Familia familia = familiaRepository
                    .findById(dto.getFamilyId())
                    .orElseThrow(() ->
                            new RuntimeException(
                                    "Família não encontrada"
                            )
                    );

            personagem.setFamilia(familia);

        } else {

            personagem.setFamilia(null);
        }

        Personagem atualizado =
                personagemRepository.save(personagem);

        return new PersonagemResponseDTO(
                atualizado,
                true
        );
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

    private String extrairUrlLimpa(
            String urlRaw
    ) {

        if (urlRaw == null
                || urlRaw.isBlank()) {

            return null;
        }

        String url = urlRaw.trim();

        if (url.startsWith("{")
                && url.contains("\"url\"")) {

            try {

                return url
                        .split("\"url\"\\s*:\\s*\"")[1]
                        .split("\"")[0];

            } catch (Exception e) {

                return url;
            }
        }

        return url;
    }
}