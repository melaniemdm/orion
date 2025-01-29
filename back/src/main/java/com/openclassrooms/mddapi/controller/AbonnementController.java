package com.openclassrooms.mddapi.controller;

import com.openclassrooms.mddapi.model.Abonnement;
import com.openclassrooms.mddapi.repository.AbonnementRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("user/1/subscribe")
public class AbonnementController {
@Autowired
    AbonnementRepository abonnementRepository;

public AbonnementController(AbonnementRepository abonnementRepository){
    this.abonnementRepository = abonnementRepository;
}
@GetMapping
    public ResponseEntity<List<Abonnement>> getAllAbonnement(){
    return new ResponseEntity<>(abonnementRepository.findAll(), HttpStatus.OK);
}
@PostMapping
    public ResponseEntity<Abonnement> createAbonnement(Abonnement abonnement){
    Abonnement abonnementCreated = abonnementRepository.save(abonnement);
    return new ResponseEntity<>(abonnementCreated, HttpStatus.CREATED);
}
@DeleteMapping("/{id}")
public ResponseEntity<Void> deleteAbonnement(@PathVariable Long id){
    Optional<Abonnement> abonnement = abonnementRepository.findById(id);
    if (abonnement.isPresent()){
        abonnementRepository.delete(abonnement.get());
        return new ResponseEntity<>(HttpStatus.OK);
    }return new ResponseEntity<>(HttpStatus.NOT_FOUND);
}
}
