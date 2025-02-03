package com.openclassrooms.mddapi.controller;

import com.openclassrooms.mddapi.dto.AbonnementDTO;
import com.openclassrooms.mddapi.service.AbonnementService;
import com.openclassrooms.mddapi.service.JwtService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("user/subscribe")
public class AbonnementController {
@Autowired
    AbonnementService abonnementService;
    @Autowired
    private JwtService jwtService;

public AbonnementController(AbonnementService abonnementService){
    this.abonnementService = abonnementService;
}
@GetMapping
public ResponseEntity<Map<String, Object>> getAbonnementByUserId(@RequestHeader("Authorization") String authHeader) {
    try {
    Long id = extractUserIdFromToken(authHeader);

    List<AbonnementDTO> userIdList =  abonnementService.getAbonnementByUserId(id);

    return ResponseEntity.ok(Map.of("subscribe", userIdList ));
    } catch (IllegalArgumentException exception) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of("error", exception.getMessage()));
    }
}
@PostMapping
public ResponseEntity<Map<String, AbonnementDTO>> createAbonnement(
        @RequestHeader("Authorization") String authHeader,
        @RequestBody AbonnementDTO abonnementDTO) {

    try {
        // 🔥Extraire l'ID utilisateur depuis le token
        Long userId = extractUserIdFromToken(authHeader);

        //  Convertir Long en Integer
        Integer userIdAsInteger = Math.toIntExact(userId);

        // Assigner l'ID utilisateur au DTO
        abonnementDTO.setUser_id(userIdAsInteger);

        // Créer l'abonnement
        AbonnementDTO createdDTO = abonnementService.createAbonnement(abonnementDTO);

        return new ResponseEntity<>(Map.of("subscribe", createdDTO), HttpStatus.CREATED);
    } catch (IllegalArgumentException e) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                .body(Map.of("error", new AbonnementDTO()));
    }
}

@DeleteMapping("/{subscribeId}")
public ResponseEntity<Map<String, String>> deleteAbonnement(@PathVariable Long subscribeId){
    abonnementService.deleteAbonnement(subscribeId);

    return ResponseEntity.ok(Map.of("subscribe", "subscribe deleted"));
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
