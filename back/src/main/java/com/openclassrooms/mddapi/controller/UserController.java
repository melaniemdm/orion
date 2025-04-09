package com.openclassrooms.mddapi.controller;


import com.openclassrooms.mddapi.dto.UserDTO;
import com.openclassrooms.mddapi.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/user")
public class UserController {
    @Autowired
    private UserService userService;

    /**
     * Constructs a new {@link UserController} and injects the {@link UserService}.
     *
     * @param userService The service responsible for handling user-related business logic.
     */
    public UserController(UserService userService) {
        this.userService = userService;
    }

    /**
     * Retrieves a list of all users.
     *
     * @return A {@link ResponseEntity} containing a map with a list of {@link UserDTO} objects representing all users.
     */
    @GetMapping
    public ResponseEntity<Map<String, List<UserDTO>>> getAllUser() {

        // Fetch all users from the service
        List<UserDTO> userDTOS = userService.getAllUser();

        // Return the response with a list of users
        return ResponseEntity.ok(Map.of("user", userDTOS));
    }

    /**
     * Deletes a user by their unique identifier.
     *
     * @param id The unique identifier of the user to be deleted.
     * @return A {@link ResponseEntity} containing a confirmation message if the deletion is successful.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Map<String, String>> deleteUser(@PathVariable Long id) {
        // Delete the user by ID
        userService.deleteUser(id);

        // Return a confirmation response
        return ResponseEntity.ok(Map.of("user", "user deleted"));
    }

    /**
     * Updates an existing user with new information.
     *
     * @param id      The unique identifier of the user to be updated.
     * @param userDTO The {@link UserDTO} containing the updated user details.
     * @return A {@link ResponseEntity} containing the updated user if successful,
     * or a 404 NOT FOUND status if the user does not exist.
     */
    @PutMapping("/{id}")
    public ResponseEntity<Map<String, UserDTO>> updateUser(@PathVariable Long id, @RequestBody UserDTO userDTO) {
        // Attempt to update the user
        Optional<UserDTO> updatedOpt = userService.updateUser(id, userDTO);

        // Check if the user exists
        if (updatedOpt.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        // Return the updated user information
        return ResponseEntity.ok(Map.of("user", updatedOpt.get()));
    }

    /**
     * Retrieves a user by their unique identifier.
     *
     * @param id The unique identifier of the user to retrieve.
     * @return A {@link ResponseEntity} containing:
     * - The user's username if found (HTTP 200 OK).
     * - A 404 NOT FOUND response if the user does not exist.
     */
    @GetMapping("/{id}")
    public ResponseEntity<Map<String, String>> getUserById(@PathVariable Long id) {
        // Attempt to retrieve the user by ID
        Optional<UserDTO> userOpt = userService.getUserById(id);

        // Check if the user exists
        if (userOpt.isPresent()) {
            return ResponseEntity.ok(Map.of("user_name", userOpt.get().getUser_name()));
        } else {
            // Return a 404 response if the user is not found
            return ResponseEntity.notFound().build();
        }
    }
}
