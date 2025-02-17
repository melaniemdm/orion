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
@RequestMapping("/subscription")
public class AbonnementController {
    @Autowired
    AbonnementService abonnementService;
    @Autowired
    private JwtService jwtService;

    /**
     * Constructor of the AbonnementController.
     *
     * @param abonnementService The subscription management service injected to handle requests.
     */
    public AbonnementController(AbonnementService abonnementService) {
        this.abonnementService = abonnementService;
    }

    /**
     * Retrieves the list of subscriptions for the authenticated user by extracting their ID from the JWT token.
     *
     * @param authHeader The HTTP "Authorization" header containing the JWT token.
     * @return An HTTP response containing:
     * - The list of user subscriptions as {@link AbonnementDTO} if the request is successful.
     * - An HTTP 401 (UNAUTHORIZED) error if the token is invalid or missing.
     */
    @GetMapping
    public ResponseEntity<Map<String, Object>> getAbonnementByUserId(@RequestHeader("Authorization") String authHeader) {
        try {
            // Extract the user ID from the JWT token
            Long id = extractUserIdFromToken(authHeader);

            // Retrieve the list of subscriptions associated with the user
            List<AbonnementDTO> userIdList = abonnementService.getAbonnementByUserId(id);

            // Return the list of subscriptions
            return ResponseEntity.ok(Map.of("subscribe", userIdList));

        } catch (IllegalArgumentException exception) {
            // Handle errors related to the JWT token
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of("error", exception.getMessage()));
        }
    }

    /**
     * Creates a new subscription for the authenticated user by extracting their ID from the JWT token.
     *
     * @param authHeader    The HTTP "Authorization" header containing the JWT token.
     * @param abonnementDTO The subscription details to be created in DTO format.
     * @return An HTTP response containing:
     * - The created subscription as a {@link AbonnementDTO} with a 201 (CREATED) status if the request is successful.
     * - A 401 (UNAUTHORIZED) error if the token is invalid or not provided.
     */
    @PostMapping
    public ResponseEntity<Map<String, AbonnementDTO>> createAbonnement(@RequestHeader("Authorization") String authHeader, @RequestBody AbonnementDTO abonnementDTO) {

        try {
            // Extract the user ID from the token
            Long userId = extractUserIdFromToken(authHeader);

            // Conversion: Long to Integer
            Integer userIdAsInteger = Math.toIntExact(userId);

            // Assign the user ID to the DTO
            abonnementDTO.setUser_id(userIdAsInteger);

            // Create the subscription
            AbonnementDTO createdDTO = abonnementService.createAbonnement(abonnementDTO);

            return new ResponseEntity<>(Map.of("subscribe", createdDTO), HttpStatus.CREATED);
        } catch (IllegalArgumentException exception) {
            // Handle authentication-related errors
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of("error", new AbonnementDTO()));
        }
    }

    /**
     * Deletes a specific subscription for the authenticated user.
     *
     * @param authHeader  The HTTP "Authorization" header containing the JWT token.
     * @param subscribeId The ID of the subscription to be deleted.
     * @return An HTTP response containing:
     * - A confirmation message with status 200 (OK) if the subscription is successfully deleted.
     * - A 404 (NOT FOUND) error if the subscription does not exist.
     * - A 403 (FORBIDDEN) error if the user is not authorized to delete this subscription.
     * - A 401 (UNAUTHORIZED) error if the JWT token is invalid or missing.
     */
    @DeleteMapping("/{subscribeId}")
    public ResponseEntity<Map<String, String>> deleteAbonnement(@RequestHeader("Authorization") String authHeader, @PathVariable Long subscribeId) {

        try {
            // Extract the user ID from the token
            Long userIdFromToken = extractUserIdFromToken(authHeader);
            Integer userIdAsInteger = Math.toIntExact(userIdFromToken);

            // Retrieve all subscriptions of the user
            List<AbonnementDTO> abonnements = abonnementService.getAbonnementByUserId(Long.valueOf(userIdAsInteger));

            // Check if the requested subscription belongs to the user
            Optional<AbonnementDTO> abonnement = abonnements.stream().filter(abonnementDTO -> abonnementDTO.getId().equals(subscribeId)).findFirst();

            // Verify if the subscription exists
            if (abonnement.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("error", "Abonnement not found"));
            }

            // Check if `user_id` is `null` before comparing
            Integer abonnementUserId = abonnement.get().getUser_id();
            if (abonnementUserId == null || !abonnementUserId.equals(userIdAsInteger)) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body(Map.of("error", "You are not authorized to delete this subscription"));
            }

            // Delete the subscription
            abonnementService.deleteAbonnement(subscribeId);
            return ResponseEntity.ok(Map.of("subscribe", "subscribe deleted"));

        } catch (IllegalArgumentException exception) {
            // Handle authentication-related errors
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of("error", "Invalid token or authentication issue"));
        }
    }

    /**
     * Extracts the user ID from the JWT token provided in the Authorization header.
     *
     * @param authHeader The HTTP "Authorization" header containing the JWT token.
     * @return The user ID extracted from the token.
     * @throws IllegalArgumentException If the token is missing, invalid, or does not contain a user ID.
     */
    private Long extractUserIdFromToken(String authHeader) {
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            throw new IllegalArgumentException("Missing or invalid token");
        }

        // Extract the token (without "Bearer ")
        String token = authHeader.substring(7);

        // Extract the user ID from the token
        Long userId = jwtService.extractUserId(token);

        if (userId == null) {
            throw new IllegalArgumentException("Invalid token");
        }

        return userId;
    }


}
