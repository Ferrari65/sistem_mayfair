package com.Rpg.sistem_mayfair.service;

import com.Rpg.sistem_mayfair.domain.Enum.Genero;
import com.Rpg.sistem_mayfair.domain.Enum.StatusCivil;
import com.Rpg.sistem_mayfair.domain.Familia;
import com.Rpg.sistem_mayfair.domain.Personagem;
import com.Rpg.sistem_mayfair.domain.Player;
import com.Rpg.sistem_mayfair.dto.personagem.PersonagemDTO;
import com.Rpg.sistem_mayfair.repository.FamiliaRepository;
import com.Rpg.sistem_mayfair.repository.PersonagemRepository;
import com.Rpg.sistem_mayfair.repository.PlayerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PersonagemService {

    private final PersonagemRepository personagemRepository;
    private final FamiliaRepository familiaRepository;
    private final PlayerRepository playerRepository;


    // ============================================================
    // CRIAR PERSONAGEM
    // ============================================================

    public Personagem criar(PersonagemDTO dto) {

        Personagem personagem = new Personagem();

        personagem.setNome(dto.getName());
        personagem.setIdade(dto.getAge());
        personagem.setTitulo(dto.getTitle());

        personagem.setPrestigio(
                dto.getPrestige() != null
                        ? dto.getPrestige()
                        : 20
        );

        personagem.setGenero(
                dto.getGenero() != null
                        ? dto.getGenero()
                        : Genero.NAO_INFORMADO
        );

        personagem.setStatusCivil(
                dto.getStatusCivil() != null
                        ? dto.getStatusCivil()
                        : StatusCivil.SOLTEIRO
        );


        // PARCEIRO
        if (dto.getParceiroId() != null) {

            Personagem parceiro = personagemRepository
                    .findById(dto.getParceiroId())
                    .orElseThrow(() ->
                            new RuntimeException(
                                    "Parceiro não encontrado"
                            )
                    );

            personagem.setParceiro(parceiro);
        }


        personagem.setDescricao(dto.getDescription());

        personagem.setShape(dto.getShape());

        personagem.setImageUrl(dto.getImageUrl());


        // FAMÍLIA
        if (dto.getFamilyId() != null &&
                dto.getFamilyId() > 0) {

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

        return personagemRepository.save(personagem);
    }


    // ============================================================
    // LISTAR PERSONAGENS
    // ============================================================

    public List<PersonagemDTO> listar(boolean isAdmin) {

        return personagemRepository.findAll()
                .stream()
                .map(p -> toDTO(p, isAdmin))
                .toList();
    }


    // ============================================================
    // ATRIBUIR PLAYER
    // ============================================================

    public Personagem atribuirPlayer(
            Long personagemId,
            Long playerId
    ) {

        Personagem personagem = personagemRepository
                .findById(personagemId)
                .orElseThrow(() ->
                        new RuntimeException(
                                "Personagem não encontrado"
                        )
                );

        Player player = playerRepository
                .findById(playerId)
                .orElseThrow(() ->
                        new RuntimeException(
                                "Player não encontrado"
                        )
                );

        personagem.setPlayer(player);

        return personagemRepository.save(personagem);
    }


    // ============================================================
    // ENTITY -> DTO
    // ============================================================

    private PersonagemDTO toDTO(
            Personagem p,
            boolean isAdmin
    ) {

        PersonagemDTO dto = new PersonagemDTO();

        dto.setName(p.getNome());
        dto.setAge(p.getIdade());
        dto.setTitle(p.getTitulo());
        dto.setPrestige(p.getPrestigio());
        dto.setDescription(p.getDescricao());
        dto.setImageUrl(p.getImageUrl());

        if (p.getFamilia() != null) {

            dto.setFamilyId(
                    p.getFamilia().getId()
            );
        }

        dto.setGenero(p.getGenero());

        dto.setStatusCivil(p.getStatusCivil());

        if (p.getParceiro() != null) {

            dto.setParceiroId(
                    p.getParceiro().getId_personagens()
            );
        }

        if (isAdmin) {

            dto.setShape(p.getShape());

        } else {

            dto.setShape(null);
        }

        return dto;
    }


    // ============================================================
    // ATUALIZAR
    // ============================================================

    public Personagem atualizar(
            Long id,
            PersonagemDTO dto
    ) {

        Personagem personagem = personagemRepository
                .findById(id)
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

        if (dto.getPrestige() != null) {
            personagem.setPrestigio(dto.getPrestige());
        }

        if (dto.getDescription() != null) {
            personagem.setDescricao(
                    dto.getDescription()
            );
        }

        if (dto.getShape() != null) {
            personagem.setShape(dto.getShape());
        }

        if (dto.getImageUrl() != null) {
            personagem.setImageUrl(
                    dto.getImageUrl()
            );
        }


        // FAMÍLIA
        if (dto.getFamilyId() != null) {

            Familia familia = familiaRepository
                    .findById(dto.getFamilyId())
                    .orElseThrow(() ->
                            new RuntimeException(
                                    "Família não encontrada"
                            )
                    );

            personagem.setFamilia(familia);
        }


        // GENERO
        if (dto.getGenero() != null) {

            personagem.setGenero(
                    dto.getGenero()
            );
        }


        // STATUS CIVIL
        if (dto.getStatusCivil() != null) {

            personagem.setStatusCivil(
                    dto.getStatusCivil()
            );
        }


        // PARCEIRO
        if (dto.getParceiroId() != null) {

            Personagem parceiro = personagemRepository
                    .findById(dto.getParceiroId())
                    .orElseThrow(() ->
                            new RuntimeException(
                                    "Parceiro não encontrado"
                            )
                    );

            personagem.setParceiro(parceiro);
        }

        return personagemRepository.save(personagem);
    }


    // ============================================================
    // EXCLUIR PERSONAGEM
    // ============================================================

    @Transactional
    public void deletar(Long id) {

        // --------------------------------------------------------
        // 1. BUSCAR PERSONAGEM
        // --------------------------------------------------------

        Personagem personagem = personagemRepository
                .findById(id)
                .orElseThrow(() ->
                        new RuntimeException(
                                "Personagem não encontrado"
                        )
                );


        // --------------------------------------------------------
        // 2. REMOVER COMO PARCEIRO DE OUTROS PERSONAGENS
        // --------------------------------------------------------

        List<Personagem> relacionados =
                personagemRepository
                        .findByParceiro(personagem);

        if (!relacionados.isEmpty()) {

            for (Personagem outro : relacionados) {

                outro.setParceiro(null);
            }

            personagemRepository.saveAll(
                    relacionados
            );
        }


        // --------------------------------------------------------
        // 3. DESVINCULAR FAMÍLIA
        // --------------------------------------------------------
        //
        // NÃO apagamos a família.
        // Apenas removemos a referência do personagem.
        // --------------------------------------------------------

        personagem.setFamilia(null);


        // --------------------------------------------------------
        // 4. DESVINCULAR PLAYER
        // --------------------------------------------------------
        //
        // NÃO apagamos o player.
        // Apenas removemos a referência.
        // --------------------------------------------------------

        personagem.setPlayer(null);


        // --------------------------------------------------------
        // 5. SALVAR AS ALTERAÇÕES
        // --------------------------------------------------------

        personagemRepository.save(personagem);


        // --------------------------------------------------------
        // 6. EXCLUIR PERSONAGEM
        // --------------------------------------------------------
        //
        // HistoricoPrestigio será excluído por:
        //
        // cascade = CascadeType.ALL
        // orphanRemoval = true
        //
        // Jornais serão desvinculados pelo @PreRemove.
        // --------------------------------------------------------

        personagemRepository.delete(personagem);
    }
}