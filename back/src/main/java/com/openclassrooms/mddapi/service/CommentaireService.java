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

    /**
     * Constructs a new instance of {@link CommentaireService}.
     *
     * @param commentaireRepository The repository used to manage comments in the database.
     */
    public CommentaireService(CommentaireRepository commentaireRepository) {
        this.commentaireRepository = commentaireRepository;
    }

    /**
     * Retrieves a list of all comments from the database.
     *
     * @return A list of {@link CommentaireDTO} representing all available comments.
     */
    public List<CommentaireDTO> getAllCommentaire() {

        // Fetch all comments from the repository
        List<Commentaire> commentaires = (List<Commentaire>) commentaireRepository.findAll();

        // Convert the list of Commentaire entities to CommentaireDTO objects
        List<CommentaireDTO> commentaireDTOS = new ArrayList<>();

        for (Commentaire commentaire : commentaires) {
            commentaireDTOS.add(entityToDto(commentaire));
        }
        // Return the list of DTOs
        return commentaireDTOS;
    }

    /**
     * Retrieves all comments associated with a specific article.
     *
     * @param articleId The ID of the article for which comments should be retrieved.
     * @return A list of {@link CommentaireDTO} containing all comments related to the given article.
     */
    public List<CommentaireDTO> getCommentairesByArticle(Integer articleId) {
        // Fetch all comments from the repository
        List<Commentaire> commentaires = (List<Commentaire>) commentaireRepository.findAll();

        // Create a list to store the filtered comments
        List<CommentaireDTO> resultat = new ArrayList<>();

        // Filter comments by the provided article ID
        for (Commentaire commentaire : commentaires) {

            if (commentaire.getArticle_id().equals(articleId)) {
                // Convert entity to DTO and add to the result list
                resultat.add(entityToDto(commentaire));
            }
        }
        // Return the filtered list of comments
        return resultat;
    }

    /**
     * Creates a new comment and saves it in the database.
     *
     * @param dto The {@link CommentaireDTO} object containing the comment details to be created.
     * @return A {@link CommentaireDTO} representing the newly created comment with its generated ID.
     */
    public CommentaireDTO createCommentaire(CommentaireDTO dto) {

        // Create a new Commentaire entity from the provided DTO
        Commentaire commentaire = new Commentaire();
        commentaire.setId(dto.getId());
        commentaire.setAuteur_id(dto.getAuteur_id());
        commentaire.setArticle_id(dto.getArticle_id());
        commentaire.setCommentary(dto.getCommentary());

        // Save the new comment in the repository (database)
        Commentaire savedCommentaire = commentaireRepository.save(commentaire);

        // Convert the saved entity back to DTO for the response
        CommentaireDTO commentaireDTO = new CommentaireDTO();
        commentaireDTO.setId(savedCommentaire.getId());
        commentaireDTO.setAuteur_id(savedCommentaire.getAuteur_id());
        commentaireDTO.setArticle_id(savedCommentaire.getArticle_id());
        commentaireDTO.setCommentary(savedCommentaire.getCommentary());

        // Return the created comment as a DTO
        return commentaireDTO;
    }

    /**
     * Converts a {@link Commentaire} entity to a {@link CommentaireDTO}.
     *
     * @param commentaire The entity to be converted.
     * @return A {@link CommentaireDTO} object containing the same data as the entity.
     */
    private CommentaireDTO entityToDto(Commentaire commentaire) {

        // Create a new DTO object
        CommentaireDTO commentaireDTO = new CommentaireDTO();

        // Map fields from the entity to the DTO
        commentaireDTO.setId(commentaire.getId());
        commentaireDTO.setArticle_id(commentaire.getArticle_id());
        commentaireDTO.setAuteur_id(commentaire.getAuteur_id());
        commentaireDTO.setCommentary(commentaireDTO.getCommentary());

        // Return the converted DTO
        return commentaireDTO;
    }

    /**
     * Converts a {@link CommentaireDTO} to a {@link Commentaire} entity.
     *
     * @param commentaireDTO The DTO object to be converted.
     * @return A {@link Commentaire} entity containing the same data as the DTO.
     */
    private Commentaire dtoToEntity(CommentaireDTO commentaireDTO) {

        // Create a new entity object
        Commentaire commentaire = new Commentaire();

        // Map fields from DTO to entity
        commentaire.setId(commentaireDTO.getId());
        commentaire.setArticle_id(commentaireDTO.getArticle_id());
        commentaire.setAuteur_id(commentaireDTO.getAuteur_id());
        commentaire.setCommentary(commentaireDTO.getCommentary());

        // Return the converted entity
        return commentaire;
    }
}
