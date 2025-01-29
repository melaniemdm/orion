package com.openclassrooms.mddapi.controller;

import com.openclassrooms.mddapi.dto.AbonnementDTO;
import com.openclassrooms.mddapi.service.AbonnementService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("user/1/subscribe")
public class AbonnementController {
@Autowired
    AbonnementService abonnementService;

public AbonnementController(AbonnementService abonnementService){
    this.abonnementService = abonnementService;
}
@GetMapping
public ResponseEntity<Map<String, List<AbonnementDTO>>> getAllAbonnement (){
    List<AbonnementDTO> abonnementDTOS = abonnementService.getAllAbonnement();

    return ResponseEntity.ok(Map.of("subscribe", abonnementDTOS));
}
@PostMapping
public ResponseEntity<Map<String, AbonnementDTO>> createAbonnement(@RequestBody AbonnementDTO abonnementDTO) {
    AbonnementDTO createdDTO = abonnementService.createAbonnement(abonnementDTO);

    return new ResponseEntity<>(Map.of("subscribe", createdDTO), HttpStatus.CREATED);
}
@DeleteMapping("/{id}")
public ResponseEntity<Map<String, String>> deleteAbonnement(@PathVariable Long id){
    abonnementService.deleteAbonnement(id);

    return ResponseEntity.ok(Map.of("subscribe", "subscribe deleted"));
}
}
