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
import java.util.Optional;

@RestController
@RequestMapping("user/subscription")
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
    } catch (IllegalArgumentException exception) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                .body(Map.of("error", new AbonnementDTO()));
    }
}

    @DeleteMapping("/{subscribeId}")
    public ResponseEntity<Map<String, String>> deleteAbonnement(
            @RequestHeader("Authorization") String authHeader,
            @PathVariable Long subscribeId) {

        try {
            //  Extraire l’ID utilisateur depuis le token
            Long userIdFromToken = extractUserIdFromToken(authHeader);
            Integer userIdAsInteger = Math.toIntExact(userIdFromToken);

            //  Récupérer tous les abonnements de l'utilisateur
            List<AbonnementDTO> abonnements = abonnementService.getAbonnementByUserId(Long.valueOf(userIdAsInteger));

            //  Vérifier si l'abonnement demandé appartient bien à l'utilisateur
            Optional<AbonnementDTO> abonnement = abonnements.stream()
                    .filter(abonnementDTO -> abonnementDTO.getId().equals(subscribeId))
                    .findFirst();

            // Vérifier si l’abonnement existe
            if (abonnement.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(Map.of("error", "Abonnement not found"));
            }

            // Vérifier si `user_id` est `null` avant de comparer
            Integer abonnementUserId = abonnement.get().getUser_id();
            if (abonnementUserId == null || !abonnementUserId.equals(userIdAsInteger)) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN)
                        .body(Map.of("error", "You are not authorized to delete this subscription"));
            }

            // Supprimer l’abonnement
            abonnementService.deleteAbonnement(subscribeId);
            return ResponseEntity.ok(Map.of("subscribe", "subscribe deleted"));

        } catch (IllegalArgumentException exception) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(Map.of("error", "Invalid token or authentication issue"));
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
