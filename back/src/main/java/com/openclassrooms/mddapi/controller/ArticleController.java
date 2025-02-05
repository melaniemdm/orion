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
@RequestMapping("post")
public class ArticleController {
@Autowired
private ArticleService articleService;
@Autowired
private JwtService jwtService;

    public ArticleController(ArticleService articleService) {
        this.articleService = articleService;
    }


    @GetMapping
    public ResponseEntity<Map<String, List<ArticleDTO>>> getAllArticle() {
        List<ArticleDTO> articleDTOS = articleService.getAllArticle();

        return ResponseEntity.ok(Map.of("post", articleDTOS));
    }

    @PostMapping
    public ResponseEntity<Map<String, Object>> createArticle(@RequestBody ArticleDTO articleDTO, @RequestHeader("Authorization") String authHeader) {
        try{
            // 🔥Extrait l'ID utilisateur depuis le token
            Long userId = extractUserIdFromToken(authHeader);

            //  Converti Long en Integer
            Integer userIdAsInteger = Math.toIntExact(userId);

            // Assigne l'ID utilisateur au DTO
            articleDTO.setAuteur_id(userIdAsInteger);
            ArticleDTO createdDTO = articleService.createArticle(articleDTO);

            return new ResponseEntity<>(Map.of("post", createdDTO), HttpStatus.CREATED);
        }catch (IllegalArgumentException exception){
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(Map.of("error", new ArticleDTO()));
        }


    }



    @GetMapping("/{id}")
    public ResponseEntity<Map<String, ArticleDTO>> getArticleById(@PathVariable Long id) {
        Optional<ArticleDTO> articleOpt = articleService.getArticleById(id);

        if (articleOpt.isPresent()) {

            return ResponseEntity.ok(Map.of("post", articleOpt.get()));
        } else {
            return ResponseEntity.notFound().build();
        }
    }


    @PutMapping("/{id}")
    public ResponseEntity<Map<String, Object>> updateArticle(
            @PathVariable Long id,
            @RequestBody ArticleDTO articleDTO,
            @RequestHeader("Authorization") String authHeader
    ) {

      try{
          // 🔥Extrait l'ID utilisateur depuis le token
          Long userId = extractUserIdFromToken(authHeader);

          //  Converti Long en Integer
          Integer userIdAsInteger = Math.toIntExact(userId);
          articleDTO.setAuteur_id(userIdAsInteger);

          Optional<ArticleDTO> updatedOpt = articleService.updateArticle(id, articleDTO);

          if (updatedOpt.isEmpty()) {
              return ResponseEntity.notFound().build();
          }

          return ResponseEntity.ok(Map.of("post", updatedOpt.get()));
      }catch (IllegalArgumentException exception){
          return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                  .body(Map.of("error", new ArticleDTO()));
      }


    }


    @DeleteMapping("/{id}")
    public ResponseEntity<Map<String, String>> deleteArticle(@PathVariable Long id,@RequestHeader("Authorization") String authHeader){
        try {
            //  Extrait l’ID utilisateur depuis le token
            Long userIdFromToken = extractUserIdFromToken(authHeader);
            Integer userIdAsInteger = Math.toIntExact(userIdFromToken);

            //  Récupère tous les articles de l'utilisateur
            Optional<ArticleDTO> articles = articleService.getArticleById(id);

            //  Vérifie si l'article demandé appartient bien à l'utilisateur
            Optional<ArticleDTO> article = articles.stream()
                    .filter(articleDTO -> articleDTO.getId().equals(id))
                    .findFirst();

            // Vérifie si l’article existe
            if (article.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(Map.of("error", "article not found"));
            }

            // Vérifier si `user_id` est `null` avant de comparer
            Integer articleUserId = article.get().getAuteur_id();
            if (articleUserId == null || !articleUserId.equals(userIdAsInteger)) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN)
                        .body(Map.of("error", "You are not authorized to delete this article"));
            }

            // Supprime l’article
            articleService.deleteArticle(id);
            return ResponseEntity.ok(Map.of("post", "article deleted"));

        } catch (IllegalArgumentException exception) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(Map.of("error", "Invalid token or authentication issue"));
        }

    }


    private Long extractUserIdFromToken(String authHeader) {
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            throw new IllegalArgumentException("Missing or invalid token");
        }

        // Extrait le token (sans "Bearer ")
        String token = authHeader.substring(7);

        // Extrait l'ID utilisateur depuis le token
        Long userId = jwtService.extractUserId(token);

        if (userId == null) {
            throw new IllegalArgumentException("Invalid token");
        }

        return userId;
    }

}
