package com.openclassrooms.mddapi.controller;


import com.openclassrooms.mddapi.dto.UserDTO;
import com.openclassrooms.mddapi.service.JwtService;
import com.openclassrooms.mddapi.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@RestController
@RequestMapping("/auth")
public class AuthentificationController {
    private static final Logger logger = LoggerFactory.getLogger(AuthentificationController.class);
    @Autowired
    private UserService userService;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private JwtService jwtService;

    /**
     * Constructor for the AuthentificationController.
     * <p>
     * This constructor is used to inject the UserService, which handles user-related operations.
     * The UserService is required for authentication and other user management tasks.
     *
     * @param userService The {@link UserService} to be injected into this controller.
     */
    public AuthentificationController(UserService userService) {
        this.userService = userService;
    }

    /**
     * Handles user registration by creating a new user and returning an authentication token.
     * <p>
     * This method processes a POST request for user registration. It checks if the provided password is valid,
     * saves the user data, and generates a JWT token for authentication. The response includes the generated token.
     *
     * @param userDTO The {@link UserDTO} object containing user information to be saved.
     * @return A {@link ResponseEntity} containing:
     * - A 400 Bad Request response if the password is missing or empty.
     * - A 200 OK response with the token and user information if the registration is successful.
     */
    @PostMapping("/register")
    public ResponseEntity<?> createUser(@RequestBody UserDTO userDTO) {

        // Check if the password is present and not empty
        if (userDTO.getPassword() == null || userDTO.getPassword().trim().isEmpty()) {
            // If the password is missing, return a 400 Bad Request response
            return ResponseEntity.badRequest().body("Password is required");
        }
        // Save the user information using the UserService
        UserDTO createdDTO = userService.saveUser(userDTO);
        // Generate a JWT token for the user
        String token = JwtService.generateToken(createdDTO.getId(), createdDTO.getEmail());

        // Return the token in the response body as a JSON object
        return ResponseEntity.ok().body("{ \"token\": \"" + token + "\" }");
    }

    /**
     * Handles user login by verifying the credentials and returning a JWT token if valid.
     * <p>
     * This method processes a POST request for user login. It checks the provided email and password, verifies
     * the credentials, and generates a JWT token for successful authentication. The response includes the generated token.
     *
     * @param loginRequest The {@link UserDTO} object containing the user's login credentials (email and password).
     * @return A {@link ResponseEntity} containing:
     * - A 401 Unauthorized response if the email or password is incorrect.
     * - A 200 OK response with the token if login is successful.
     */
    @CrossOrigin(origins = "http://localhost:4200")
    @PostMapping("/login")
    public ResponseEntity<?> loginUser(@RequestBody UserDTO loginRequest) {

        // Retrieve the user by email from the database
        Optional<UserDTO> optionalUser = userService.getUserByEmailOrUsername(loginRequest.getEmail());

        // Check if the user exists in the database
        if (optionalUser.isEmpty()) {
            logger.warn(" Utilisateur non trouvé pour l'email: {}", loginRequest.getEmail());
            // If no user is found, return a 401 Unauthorized response
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid email or password.");
        }

        // Extract the user details from the Optional
        UserDTO userDTO = optionalUser.get();

        // Check if the provided password matches the stored password
        boolean isPasswordValid = passwordEncoder.matches(loginRequest.getPassword(), userDTO.getPassword());

        // If the password is invalid, return a 401 Unauthorized response
        if (!isPasswordValid) {
            logger.warn(" Mot de passe incorrect pour l'utilisateur: {}", loginRequest.getEmail());
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid email or password.");
        }
        // Generate a JWT token for the user upon successful login
        String token = JwtService.generateToken(userDTO.getId(), userDTO.getEmail());
        logger.info(" Connexion réussie pour l'utilisateur: {}", loginRequest.getEmail());

        // Return the token in the response body as a JSON object
        return ResponseEntity.ok("{ \"token\": \"" + token + "\" }");
    }

    /**
     * Retrieves the details of the authenticated user by extracting their ID from the JWT token.
     * <p>
     * This method is used to retrieve the user information associated with the JWT token. The token is expected to be provided in the
     * Authorization header. The method will validate the token, extract the user ID, and fetch the corresponding user details from the database.
     *
     * @param authHeader The HTTP Authorization header containing the JWT token.
     * @return A {@link ResponseEntity} with:
     * - The user's details (id, username, email) if the token is valid.
     * - A 401 Unauthorized response if the token is invalid or missing.
     * - A 404 Not Found response if the user is not found.
     */
    @GetMapping("/me")
    public ResponseEntity<Map<String, Object>> getMe(@RequestHeader("Authorization") String authHeader) {
        // Check if the Authorization header contains a valid token
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of("error", "Missing or invalid token"));
        }
        // Extract the token (without the "Bearer " prefix)
        String token = authHeader.substring(7);

        // Extract the user ID from the token
        Long userId = jwtService.extractUserId(token);

        // If user ID is invalid or missing, return an Unauthorized error
        if (userId == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of("error", "Invalid token"));
        }

        // Retrieve the user details from the database using the extracted user ID
        Optional<UserDTO> userDTO = userService.getUserById(userId);

        // If the user is not found, return a Not Found error
        if (userDTO.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("error", "User not found"));
        }

        // Construct the response body with the user details
        Map<String, Object> response = new HashMap<>();
        response.put("id", userDTO.get().getId());
        response.put("user_name", userDTO.get().getUser_name());
        response.put("email", userDTO.get().getEmail());

        // Return the user details in the response
        return ResponseEntity.ok(response);
    }

}
