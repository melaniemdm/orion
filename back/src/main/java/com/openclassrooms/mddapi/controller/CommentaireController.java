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
//constructeur
    public CommentaireController(CommentaireService commentaireService){
        this.commentaireService = commentaireService;
    }


    @GetMapping
    public List<CommentaireDTO> getCommentairesByArticleId(@PathVariable Integer id) {
        // Transmet l’ID au service
        System.out.println("L'ID de l'article est : " + id);
        return commentaireService.getCommentairesByArticle(id);
    }

    @PostMapping
     public ResponseEntity<Map<String, CommentaireDTO>> createCommentaire(@RequestBody CommentaireDTO commentaireDTO, @RequestHeader("Authorization") String authHeader) {
        try{
            // 🔥Extraire l'ID utilisateur depuis le token
            Long userId = extractUserIdFromToken(authHeader);

            //  Convertir Long en Integer
            Integer userIdAsInteger = Math.toIntExact(userId);

            // Assigner l'ID utilisateur au DTO
            commentaireDTO.setAuteur_id(userIdAsInteger);

            CommentaireDTO createdDTO = commentaireService.createCommentaire(commentaireDTO);
            return new ResponseEntity<>(Map.of("comment", createdDTO), HttpStatus.CREATED);
        }catch (IllegalArgumentException exception) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(Map.of("error", new CommentaireDTO()));

        }
    }

    private Long extractUserIdFromToken(String authHeader) {
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            throw new IllegalArgumentException("Missing or invalid token");
        }

        // Extraire le token (sans "Bearer ")
        String token = authHeader.substring(7);

        // Extraire l'ID utilisateur depuis le token
        Long userId = jwtService.extractUserId(token);

        if (userId == null) {
            throw new IllegalArgumentException("Invalid token");
        }

        return userId;
    }
}
