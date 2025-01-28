package com.openclassrooms.mddapi.service;

import com.openclassrooms.mddapi.dto.CommentaireDTO;

import com.openclassrooms.mddapi.model.Commentaire;

import com.openclassrooms.mddapi.repository.CommentaireRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class CommentaireService {
    @Autowired
    private CommentaireRepository commentaireRepository;

    public  CommentaireService(CommentaireRepository commentaireRepository){
        this.commentaireRepository=commentaireRepository;
    }

    public List<CommentaireDTO> getAllCommentaire() {

        List<Commentaire> commentaires = (List<Commentaire>) commentaireRepository.findAll();

        List<CommentaireDTO> commentaireDTOS = new ArrayList<>();

        for (Commentaire commentaire : commentaires) {
            commentaireDTOS.add(entityToDto(commentaire));
        }

        return commentaireDTOS;
    }

    public CommentaireDTO createCommentaire(CommentaireDTO dto) {

        Commentaire commentaire = new Commentaire();
        commentaire.setId(dto.getId());
        commentaire.setAuteur_id(dto.getAuteur_id());
        commentaire.setArticle_id(dto.getArticle_id());
        commentaire.setCommentary(dto.getCommentary());

        Commentaire savedCommentaire = commentaireRepository.save(commentaire);


        CommentaireDTO commentaireDTO = new CommentaireDTO();
        commentaireDTO.setId(savedCommentaire.getId());
        commentaireDTO.setAuteur_id(savedCommentaire.getAuteur_id());
        commentaireDTO.setArticle_id(savedCommentaire.getArticle_id());
        commentaireDTO.setCommentary(savedCommentaire.getCommentary());

        return commentaireDTO;
    }


    private CommentaireDTO entityToDto(Commentaire commentaire) {

        CommentaireDTO commentaireDTO = new CommentaireDTO();

        commentaireDTO.setId(commentaire.getId());
        commentaireDTO.setArticle_id(commentaire.getArticle_id());
        commentaireDTO.setAuteur_id(commentaire.getAuteur_id());
        commentaireDTO.setCommentary(commentaireDTO.getCommentary());

        return commentaireDTO;
    }



    private Commentaire dtoToEntity(CommentaireDTO commentaireDTO) {

        Commentaire commentaire = new Commentaire();

        commentaire.setId(commentaireDTO.getId());
        commentaire.setArticle_id(commentaireDTO.getArticle_id());
        commentaire.setAuteur_id(commentaireDTO.getAuteur_id());
        commentaire.setCommentary(commentaireDTO.getCommentary());
        return commentaire;
    }
}
