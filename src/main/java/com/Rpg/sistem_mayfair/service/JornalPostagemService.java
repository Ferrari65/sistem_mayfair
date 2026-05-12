package com.Rpg.sistem_mayfair.service;

import com.Rpg.sistem_mayfair.Enum.JornalReacaoTipo;
import com.Rpg.sistem_mayfair.domain.Personagem;
import com.Rpg.sistem_mayfair.domain.jornal.JornalLike;
import com.Rpg.sistem_mayfair.domain.jornal.JornalPostagem;
import com.Rpg.sistem_mayfair.domain.jornal.JornalReacao;
import com.Rpg.sistem_mayfair.dto.jornal.JornalPostagemRequestDTO;
import com.Rpg.sistem_mayfair.dto.jornal.JornalPostagemResponseDTO;
import com.Rpg.sistem_mayfair.dto.jornal.JornalReacaoResponseDTO;
import com.Rpg.sistem_mayfair.dto.personagem.PersonagemResumoDTO;
import com.Rpg.sistem_mayfair.repository.PersonagemRepository;
import com.Rpg.sistem_mayfair.repository.jornal.JornalLikeRepository;
import com.Rpg.sistem_mayfair.repository.jornal.JornalPostagemRepository;
import com.Rpg.sistem_mayfair.repository.jornal.JornalReacaoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

@Service
@RequiredArgsConstructor
public class JornalPostagemService {

    private final JornalPostagemRepository repository;
    private final PersonagemRepository personagemRepository;
    private final JornalLikeRepository likeRepository;
    private final JornalReacaoRepository reacaoRepository;

    public JornalPostagemResponseDTO criar(
            JornalPostagemRequestDTO dto
    ) {

        List<Personagem> personagens =
                personagemRepository.findAllById(dto.personagensIds());

        JornalPostagem postagem = JornalPostagem.builder()
                .titulo(dto.titulo())
                .noticia(dto.noticia())
                .dataCriacao(LocalDateTime.now())
                .personagens(personagens)
                .likes(0)
                .build();

        repository.save(postagem);

        return converter(postagem);
    }

    public List<JornalPostagemResponseDTO> listar() {

        return repository.findAll()
                .stream()
                .map(this::converter)
                .toList();
    }

    public void like(Long id, String ip) {

        JornalPostagem postagem = repository.findById(id)
                .orElseThrow();

        boolean jaCurtiu =
                likeRepository.existsByIpAddressAndPostagem(
                        ip,
                        postagem
                );

        if (jaCurtiu) {
            throw new RuntimeException("Você já curtiu.");
        }

        JornalLike like = JornalLike.builder()
                .ipAddress(ip)
                .postagem(postagem)
                .build();

        likeRepository.save(like);

        Integer likesAtuais = postagem.getLikes();

        if (likesAtuais == null) {
            likesAtuais = 0;
        }

        postagem.setLikes(likesAtuais + 1);

        repository.save(postagem);
    }

    public void reagir(
            Long id,
            JornalReacaoTipo tipo,
            String ip
    ) {

        JornalPostagem postagem = repository.findById(id)
                .orElseThrow();

        boolean jaReagiu =
                reacaoRepository
                        .existsByIpAddressAndPostagemAndTipo(
                                ip,
                                postagem,
                                tipo
                        );

        if (jaReagiu) {
            throw new RuntimeException(
                    "Você já reagiu com isso."
            );
        }

        JornalReacao reacao = JornalReacao.builder()
                .ipAddress(ip)
                .tipo(tipo)
                .postagem(postagem)
                .build();

        reacaoRepository.save(reacao);
    }

    private JornalPostagemResponseDTO converter(
            JornalPostagem postagem
    ) {

        List<PersonagemResumoDTO> personagens =
                postagem.getPersonagens()
                        .stream()
                        .map(personagem -> new PersonagemResumoDTO(
                                personagem.getId_personagens(),
                                personagem.getNome()
                        ))
                        .toList();

        List<JornalReacaoResponseDTO> reacoes =
                Arrays.stream(JornalReacaoTipo.values())
                        .map(tipo -> new JornalReacaoResponseDTO(
                                tipo.getDescricao(),
                                reacaoRepository.countByPostagemAndTipo(
                                        postagem,
                                        tipo
                                )
                        ))
                        .toList();

        return new JornalPostagemResponseDTO(
                postagem.getId(),
                postagem.getTitulo(),
                postagem.getNoticia(),
                postagem.getLikes(),
                postagem.getDataCriacao(),
                personagens,
                reacoes
        );
    }

    public JornalPostagemResponseDTO detalhar(Long id) {

        JornalPostagem postagem = repository.findById(id)
                .orElseThrow();

        return converter(postagem);
    }
}