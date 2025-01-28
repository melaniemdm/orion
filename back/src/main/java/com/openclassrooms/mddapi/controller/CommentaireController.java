package com.openclassrooms.mddapi.controller;

import com.openclassrooms.mddapi.dto.CommentaireDTO;

import com.openclassrooms.mddapi.service.CommentaireService;
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

//constructeur
    public CommentaireController(CommentaireService commentaireService){
        this.commentaireService = commentaireService;
    }


    @GetMapping
    public List<CommentaireDTO> getCommentairesByArticleId(@PathVariable Integer id) {
        // Transmet l’ID au service
        return commentaireService.getCommentairesByArticle(id);
    }

    @PostMapping
     public ResponseEntity<Map<String, CommentaireDTO>> createCommentaire(@RequestBody CommentaireDTO commentaireDTO) {
        CommentaireDTO createdDTO = commentaireService.createCommentaire(commentaireDTO);

        return new ResponseEntity<>(Map.of("comment", createdDTO), HttpStatus.CREATED);
    }
}
