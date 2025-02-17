package com.openclassrooms.mddapi.controller;

import com.openclassrooms.mddapi.dto.CommentaireDTO;

import com.openclassrooms.mddapi.service.CommentaireService;
import com.openclassrooms.mddapi.service.JwtService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/post/{id}/comment")
public class CommentaireController {
    @Autowired
    private CommentaireService commentaireService;
    @Autowired
    private JwtService jwtService;

    /// **
    //     * Constructs the {@link CommentaireController} with the required service dependency.
    //     *
    //     * @param commentaireService The service responsible for handling comment operations.
    //     */
    public CommentaireController(CommentaireService commentaireService) {
        this.commentaireService = commentaireService;
    }

    /**
     * Retrieves all comments associated with a specific article.
     *
     * @param id The ID of the article for which comments are retrieved.
     * @return A list of {@link CommentaireDTO} containing all comments for the given article.
     */
    @GetMapping
    public List<CommentaireDTO> getCommentairesByArticleId(@PathVariable Integer id) {
        // Logs the article ID for debugging
        System.out.println("L'ID de l'article est : " + id);
        // Calls the service to retrieve comments for the article
        return commentaireService.getCommentairesByArticle(id);
    }

    /**
     * Creates a new comment for an article.
     *
     * @param commentaireDTO The {@link CommentaireDTO} containing the comment details.
     * @param authHeader     The HTTP "Authorization" header containing the JWT token.
     * @return A response containing:
     * - The created {@link CommentaireDTO} with status 201 (CREATED) if successful.
     * - A 401 (UNAUTHORIZED) error if the token is missing or invalid.
     */
    @PostMapping
    public ResponseEntity<Map<String, CommentaireDTO>> createCommentaire(@RequestBody CommentaireDTO commentaireDTO, @RequestHeader("Authorization") String authHeader) {
        try {
            // Extract user ID from JWT token
            Long userId = extractUserIdFromToken(authHeader);

            // Convert Long to Integer
            Integer userIdAsInteger = Math.toIntExact(userId);

            // Assign the extracted user ID to the comment DTO
            commentaireDTO.setAuteur_id(userIdAsInteger);

            // Create and save the comment
            CommentaireDTO createdDTO = commentaireService.createCommentaire(commentaireDTO);

            // Return the created comment with HTTP 201
            return new ResponseEntity<>(Map.of("comment", createdDTO), HttpStatus.CREATED);
        } catch (IllegalArgumentException exception) {
            // Handle authentication errors (e.g., invalid token)
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(Map.of("error", new CommentaireDTO()));

        }
    }

    /**
     * Extracts the user ID from a JWT token found in the "Authorization" header.
     *
     * @param authHeader The HTTP "Authorization" header containing the JWT token in the format "Bearer <token>".
     * @return The extracted user ID as a {@link Long}.
     * @throws IllegalArgumentException If the token is missing, invalid, or does not contain a valid user ID.
     */
    private Long extractUserIdFromToken(String authHeader) {

        // Verify if the Authorization header is provided and correctly formatted
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            throw new IllegalArgumentException("Missing or invalid token");
        }

        // Extract the JWT token from the "Authorization" header (remove "Bearer ")
        String token = authHeader.substring(7);

        // Extract the user ID from the JWT token using JwtService
        Long userId = jwtService.extractUserId(token);

        // If no user ID is found, throw an authentication error
        if (userId == null) {
            throw new IllegalArgumentException("Invalid token");
        }

        //Return the extracted user ID
        return userId;
    }
}
