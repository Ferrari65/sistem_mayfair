package com.Rpg.sistem_mayfair.service;

import com.Rpg.sistem_mayfair.domain.Enum.Genero;
import com.Rpg.sistem_mayfair.domain.Enum.StatusCivil;
import com.Rpg.sistem_mayfair.domain.Familia;
import com.Rpg.sistem_mayfair.domain.JornalPostagem;
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

        // PRESTÍGIO
        personagem.setPrestigio(
                dto.getPrestige() != null
                        ? dto.getPrestige()
                        : 20
        );

        // GÊNERO
        personagem.setGenero(
                dto.getGenero() != null
                        ? dto.getGenero()
                        : Genero.NAO_INFORMADO
        );

        // STATUS CIVIL
        personagem.setStatusCivil(
                dto.getStatusCivil() != null
                        ? dto.getStatusCivil()
                        : StatusCivil.SOLTEIRO
        );


        // ========================================================
        // PARCEIRO
        // ========================================================

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


        // ========================================================
        // DESCRIÇÃO
        // ========================================================

        personagem.setDescricao(
                dto.getDescription()
        );


        // ========================================================
        // SHAPE
        // ========================================================

        personagem.setShape(
                dto.getShape()
        );


        // ========================================================
        // IMAGEM
        // ========================================================

        personagem.setImageUrl(
                dto.getImageUrl()
        );


        // ========================================================
        // FAMÍLIA
        // ========================================================

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

        dto.setName(
                p.getNome()
        );

        dto.setAge(
                p.getIdade()
        );

        dto.setTitle(
                p.getTitulo()
        );

        dto.setPrestige(
                p.getPrestigio()
        );

        dto.setDescription(
                p.getDescricao()
        );

        dto.setImageUrl(
                p.getImageUrl()
        );


        // FAMÍLIA
        if (p.getFamilia() != null) {

            dto.setFamilyId(
                    p.getFamilia().getId()
            );
        }


        // GÊNERO
        dto.setGenero(
                p.getGenero()
        );


        // STATUS CIVIL
        dto.setStatusCivil(
                p.getStatusCivil()
        );


        // PARCEIRO
        if (p.getParceiro() != null) {

            dto.setParceiroId(
                    p.getParceiro()
                            .getId_personagens()
            );
        }


        // SHAPE
        if (isAdmin) {

            dto.setShape(
                    p.getShape()
            );

        } else {

            dto.setShape(null);
        }


        return dto;
    }


    // ============================================================
    // ATUALIZAR PERSONAGEM
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


        // ========================================================
        // NOME
        // ========================================================

        if (dto.getName() != null) {

            personagem.setNome(
                    dto.getName()
            );
        }


        // ========================================================
        // IDADE
        // ========================================================

        if (dto.getAge() != null) {

            personagem.setIdade(
                    dto.getAge()
            );
        }


        // ========================================================
        // TÍTULO
        // ========================================================

        if (dto.getTitle() != null) {

            personagem.setTitulo(
                    dto.getTitle()
            );
        }


        // ========================================================
        // PRESTÍGIO
        // ========================================================

        if (dto.getPrestige() != null) {

            personagem.setPrestigio(
                    dto.getPrestige()
            );
        }


        // ========================================================
        // DESCRIÇÃO
        // ========================================================

        if (dto.getDescription() != null) {

            personagem.setDescricao(
                    dto.getDescription()
            );
        }


        // ========================================================
        // SHAPE
        // ========================================================

        if (dto.getShape() != null) {

            personagem.setShape(
                    dto.getShape()
            );
        }


        // ========================================================
        // IMAGEM
        // ========================================================

        if (dto.getImageUrl() != null) {

            personagem.setImageUrl(
                    dto.getImageUrl()
            );
        }


        // ========================================================
        // FAMÍLIA
        // ========================================================

        if (dto.getFamilyId() != null) {

            Familia familia = familiaRepository
                    .findById(dto.getFamilyId())
                    .orElseThrow(() ->
                            new RuntimeException(
                                    "Família não encontrada"
                            )
                    );

            personagem.setFamilia(
                    familia
            );
        }


        // ========================================================
        // GÊNERO
        // ========================================================

        if (dto.getGenero() != null) {

            personagem.setGenero(
                    dto.getGenero()
            );
        }


        // ========================================================
        // STATUS CIVIL
        // ========================================================

        if (dto.getStatusCivil() != null) {

            personagem.setStatusCivil(
                    dto.getStatusCivil()
            );
        }


        // ========================================================
        // PARCEIRO
        // ========================================================

        if (dto.getParceiroId() != null) {

            Personagem parceiro = personagemRepository
                    .findById(dto.getParceiroId())
                    .orElseThrow(() ->
                            new RuntimeException(
                                    "Parceiro não encontrado"
                            )
                    );

            personagem.setParceiro(
                    parceiro
            );
        }


        return personagemRepository.save(
                personagem
        );
    }


    // ============================================================
    // EXCLUIR PERSONAGEM
    // ============================================================

    @Transactional
    public void deletar(Long id) {

        // ========================================================
        // 1. BUSCAR PERSONAGEM
        // ========================================================

        Personagem personagem = personagemRepository
                .findById(id)
                .orElseThrow(() ->
                        new RuntimeException(
                                "Personagem não encontrado"
                        )
                );


        // ========================================================
        // 2. REMOVER O PERSONAGEM COMO PARCEIRO
        // ========================================================
        //
        // Se:
        //
        // João -> parceiro = Maria
        //
        // e Maria for excluída, precisamos fazer:
        //
        // João -> parceiro = null
        //
        // antes de apagar Maria.
        // ========================================================

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


        // ========================================================
        // 3. DESVINCULAR DA FAMÍLIA
        // ========================================================
        //
        // A família NÃO será apagada.
        // Apenas removemos a referência.
        // ========================================================

        personagem.setFamilia(null);


        // ========================================================
        // 4. DESVINCULAR DO PLAYER
        // ========================================================
        //
        // O player NÃO será apagado.
        // Apenas removemos a referência.
        // ========================================================

        personagem.setPlayer(null);


        // ========================================================
        // 5. REMOVER DOS JORNAIS
        // ========================================================
        //
        // Não apagamos o JornalPostagem.
        //
        // Apenas removemos o personagem da relação
        // ManyToMany.
        //
        // Isso remove o registro correspondente da:
        //
        // jornal_personagens
        //
        // ========================================================

        if (personagem.getJornais() != null) {

            for (JornalPostagem jornal :
                    personagem.getJornais()) {

                if (jornal.getPersonagens() != null) {

                    jornal.getPersonagens()
                            .remove(personagem);
                }
            }

            personagem.getJornais().clear();
        }


        // ========================================================
        // 6. SALVAR DESVINCULAÇÕES
        // ========================================================

        personagemRepository.save(
                personagem
        );


        // ========================================================
        // 7. EXCLUIR PERSONAGEM
        // ========================================================
        //
        // HistoricoPrestigio será excluído automaticamente
        // devido a:
        //
        // cascade = CascadeType.ALL
        // orphanRemoval = true
        //
        // ========================================================

        personagemRepository.delete(
                personagem
        );
    }
}