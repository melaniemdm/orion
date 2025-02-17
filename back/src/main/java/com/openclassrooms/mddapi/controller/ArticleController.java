package com.openclassrooms.mddapi.controller;


import com.openclassrooms.mddapi.dto.ArticleDTO;
import com.openclassrooms.mddapi.service.ArticleService;
import com.openclassrooms.mddapi.service.JwtService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/post")
public class ArticleController {
    @Autowired
    private ArticleService articleService;
    @Autowired
    private JwtService jwtService;


    /**
     * Constructs a new {@link ArticleController} with the specified {@link ArticleService}.
     *
     * @param articleService The service responsible for managing articles, injected into the controller.
     */
    public ArticleController(ArticleService articleService) {
        this.articleService = articleService;
    }

    /**
     * Retrieves a list of all articles.
     *
     * @return A {@link ResponseEntity} containing:
     * - A map with the key "post" and a list of {@link ArticleDTO} representing all articles.
     * - A status 200 (OK) if the request is successful.
     */
    @GetMapping
    public ResponseEntity<Map<String, List<ArticleDTO>>> getAllArticle() {
        // Fetch all articles from the service layer
        List<ArticleDTO> articleDTOS = articleService.getAllArticle();

        // Return the list of articles wrapped in a ResponseEntity with status 200 (OK)
        return ResponseEntity.ok(Map.of("post", articleDTOS));
    }

    /**
     * Creates a new article for the authenticated user.
     *
     * @param articleDTO The {@link ArticleDTO} object containing the article details.
     * @param authHeader The HTTP "Authorization" header containing the JWT token.
     * @return A {@link ResponseEntity} containing:
     * - The created article with key "post" and a status 201 (CREATED) if successful.
     * - A status 401 (UNAUTHORIZED) if the token is invalid or missing.
     */
    @PostMapping
    public ResponseEntity<Map<String, Object>> createArticle(@RequestBody ArticleDTO articleDTO, @RequestHeader("Authorization") String authHeader) {
        try {
            // Extract the user ID from the JWT token
            Long userId = extractUserIdFromToken(authHeader);

            // Convert Long to Integer
            Integer userIdAsInteger = Math.toIntExact(userId);

            // Assign the user ID to the DTO
            articleDTO.setAuteur_id(userIdAsInteger);

            // Create the article
            ArticleDTO createdDTO = articleService.createArticle(articleDTO);

            // Return the created article with HTTP 201 status
            return new ResponseEntity<>(Map.of("post", createdDTO), HttpStatus.CREATED);
        } catch (IllegalArgumentException exception) {
            // Handle authentication errors (invalid or missing token)
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(Map.of("error", new ArticleDTO()));
        }
    }

    /**
     * Retrieves an article by its ID.
     *
     * @param id The ID of the article to retrieve.
     * @return A {@link ResponseEntity} containing:
     * - The requested article with key "post" and status 200 (OK) if found.
     * - A status 404 (NOT FOUND) if the article does not exist.
     */
    @GetMapping("/{id}")
    public ResponseEntity<Map<String, ArticleDTO>> getArticleById(@PathVariable Long id) {
        // Fetch the article from the service
        Optional<ArticleDTO> articleOpt = articleService.getArticleById(id);

        // If the article exists, return it with HTTP 200 status
        if (articleOpt.isPresent()) {

            return ResponseEntity.ok(Map.of("post", articleOpt.get()));
        } else {
            // If not found, return HTTP 404 status
            return ResponseEntity.notFound().build();
        }
    }

    /**
     * Updates an existing article for the authenticated user.
     *
     * @param id         The ID of the article to be updated.
     * @param articleDTO The details of the article to update, provided in the request body.
     * @param authHeader The HTTP "Authorization" header containing the JWT token.
     * @return A {@link ResponseEntity} containing:
     * - The updated article with a key "post" and a status of 200 (OK) if the update succeeds.
     * - A status of 404 (NOT FOUND) if the article is not found.
     * - A status of 401 (UNAUTHORIZED) if the token is invalid or not provided.
     */
    @PutMapping("/{id}")
    public ResponseEntity<Map<String, Object>> updateArticle(
            @PathVariable Long id,
            @RequestBody ArticleDTO articleDTO,
            @RequestHeader("Authorization") String authHeader
    ) {

        try {
            // Extract the user ID from the JWT token
            Long userId = extractUserIdFromToken(authHeader);

            //  Convert the user ID from Long to Integer to match the DTO format
            Integer userIdAsInteger = Math.toIntExact(userId);

            // Assign the user ID as the article's author ID
            articleDTO.setAuteur_id(userIdAsInteger);

            // Update the article through the service
            Optional<ArticleDTO> updatedOpt = articleService.updateArticle(id, articleDTO);

            // When the article was not found in the database
            if (updatedOpt.isEmpty()) {
                return ResponseEntity.notFound().build();
            }

            // Return the updated article
            return ResponseEntity.ok(Map.of("post", updatedOpt.get()));
        } catch (IllegalArgumentException exception) {
            // Handle invalid or missing token issues
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(Map.of("error", new ArticleDTO()));
        }
    }

    /**
     * Deletes an article if the authenticated user is its author.
     *
     * @param id         The ID of the article to be deleted.
     * @param authHeader The HTTP "Authorization" header containing the JWT token.
     * @return A {@link ResponseEntity} containing:
     * - A confirmation message with status 200 (OK) if the article is successfully deleted.
     * - A status 404 (NOT FOUND) if the article does not exist.
     * - A status 403 (FORBIDDEN) if the user is not authorized to delete the article.
     * - A status 401 (UNAUTHORIZED) if the token is missing or invalid.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Map<String, String>> deleteArticle(@PathVariable Long id, @RequestHeader("Authorization") String authHeader) {
        try {
            // Extract the user ID from the JWT token
            Long userIdFromToken = extractUserIdFromToken(authHeader);
            Integer userIdAsInteger = Math.toIntExact(userIdFromToken);

            // Retrieve the article by ID
            Optional<ArticleDTO> articleOpt = articleService.getArticleById(id);


            // If the article does not exist, return 404
            if (articleOpt.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(Map.of("error", "article not found"));
            }

            // Extract article details
            ArticleDTO article = articleOpt.get();

            // Ensure the authenticated user is the article's author
            if (article.getAuteur_id() == null || !article.getAuteur_id().equals(userIdAsInteger)) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN)
                        .body(Map.of("error", "You are not authorized to delete this article"));
            }

            // Delete the article
            articleService.deleteArticle(id);
            return ResponseEntity.ok(Map.of("post", "article deleted"));

        } catch (IllegalArgumentException exception) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(Map.of("error", "Invalid token or authentication issue"));
        }

    }

    /**
     * Extracts the user ID from the provided JWT token in the Authorization header.
     *
     * @param authHeader The HTTP "Authorization" header containing the JWT token.
     * @return The extracted user ID.
     * @throws IllegalArgumentException If the token is missing, invalid, or does not contain a user ID.
     */
    private Long extractUserIdFromToken(String authHeader) {
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            throw new IllegalArgumentException("Missing or invalid token");
        }

        // Extract the token (remove "Bearer ")
        String token = authHeader.substring(7);

        // Extract the user ID from the token
        Long userId = jwtService.extractUserId(token);

        // If no user ID is found, throw an exception
        if (userId == null) {
            throw new IllegalArgumentException("Invalid token");
        }

        return userId;
    }

}
