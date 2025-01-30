package com.openclassrooms.mddapi.controller;

import com.openclassrooms.mddapi.dto.AbonnementDTO;
import com.openclassrooms.mddapi.service.AbonnementService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.persistence.criteria.CriteriaBuilder;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("user/{id}/subscribe")
public class AbonnementController {
@Autowired
    AbonnementService abonnementService;

public AbonnementController(AbonnementService abonnementService){
    this.abonnementService = abonnementService;
}
@GetMapping
public List<AbonnementDTO> getAbonnementByUserId(@PathVariable Integer id) {
    // Transmet l’ID au service
    System.out.println("L'ID de l'abonnement est : " + id);
    return abonnementService.getAbonnementByUserId(id);
}
@PostMapping
public ResponseEntity<Map<String, AbonnementDTO>> createAbonnement(@RequestBody AbonnementDTO abonnementDTO) {
    System.out.println("abonnementDTO est "+ abonnementDTO.getUser_id());
    AbonnementDTO createdDTO = abonnementService.createAbonnement(abonnementDTO);

    return new ResponseEntity<>(Map.of("subscribe", createdDTO), HttpStatus.CREATED);
}
@DeleteMapping("/{subscribeId}")
public ResponseEntity<Map<String, String>> deleteAbonnement(@PathVariable Long id){
    abonnementService.deleteAbonnement(id);

    return ResponseEntity.ok(Map.of("subscribe", "subscribe deleted"));
}
}
