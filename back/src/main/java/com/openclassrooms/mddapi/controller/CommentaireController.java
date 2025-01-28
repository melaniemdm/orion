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
@RequestMapping("/article/{id}/commentaire")
public class CommentaireController {
@Autowired
 private CommentaireService commentaireService;

//constructeur
    public CommentaireController(CommentaireService commentaireService){
        this.commentaireService = commentaireService;
    }

    @GetMapping

    public ResponseEntity<Map<String, List<CommentaireDTO>>> getAllCommentaire() {
        List<CommentaireDTO> commentaireDTOS = commentaireService.getAllCommentaire();

        return ResponseEntity.ok(Map.of("subject", commentaireDTOS));
    }

    @PostMapping
     public ResponseEntity<Map<String, CommentaireDTO>> createCommentaire(@RequestBody CommentaireDTO commentaireDTO) {
        CommentaireDTO createdDTO = commentaireService.createCommentaire(commentaireDTO);

        return new ResponseEntity<>(Map.of("subject", createdDTO), HttpStatus.CREATED);
    }
}
