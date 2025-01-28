package com.openclassrooms.mddapi.controller;

import com.openclassrooms.mddapi.model.Commentaire;
import com.openclassrooms.mddapi.repository.CommentaireRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/article/{id}/commentaire")
public class CommentaireController {
@Autowired
    CommentaireRepository commentaireRepository;

//constructeur
    public CommentaireController(CommentaireRepository commentaireRepository){
        this.commentaireRepository = commentaireRepository;
    }

    @GetMapping
    public ResponseEntity<List<Commentaire>> getAllCommentaire(){
        return new ResponseEntity<>(commentaireRepository.findAll(), HttpStatus.OK);
    }
    @PostMapping
    public ResponseEntity<Commentaire> createCommentaire(@RequestBody Commentaire commentaire ){
        Commentaire commentaireCreated = commentaireRepository.save(commentaire);
        return new ResponseEntity<>(commentaireCreated, HttpStatus.CREATED);
    }

}
