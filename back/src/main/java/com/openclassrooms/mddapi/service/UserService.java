package com.openclassrooms.mddapi.service;


import com.openclassrooms.mddapi.dto.UserDTO;
import com.openclassrooms.mddapi.model.User;
import com.openclassrooms.mddapi.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;

    /**
     * Constructs a new instance of {@link UserService} with the provided {@link UserRepository}.
     *
     * @param userRepository The repository used to interact with user data in the database.
     */
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;

    }

    /**
     * Creates a new user and saves it to the database.
     *
     * @param dto The {@link UserDTO} object containing user details.
     * @return A {@link UserDTO} representing the newly created user with its generated ID.
     */
    public UserDTO createUser(UserDTO dto) {
        // Convert DTO to entity
        User user = new User();
        user.setUsername(dto.getUser_name());
        user.setEmail(dto.getEmail());
        user.setPassword(dto.getPassword());
        // Save the user entity to the database
        User savedUser = userRepository.save(user);

        // Convert saved entity back to DTO and return
        UserDTO userDTO = new UserDTO();
        userDTO.setId(savedUser.getId());
        userDTO.setUser_name(savedUser.getUsername());
        userDTO.setEmail(savedUser.getEmail());
        userDTO.setPassword(savedUser.getPassword());

        return userDTO;
    }

    /**
     * Updates an existing user in the database.
     *
     * @param id      The ID of the user to be updated.
     * @param userDTO The {@link UserDTO} containing the updated user details.
     * @return An {@link Optional} containing the updated {@link UserDTO} if the user exists,
     * or an empty {@link Optional} if no user is found with the given ID.
     */
    public Optional<UserDTO> updateUser(Long id, UserDTO userDTO) {

        // Retrieve the user by ID
        Optional<User> userOptional = userRepository.findById(id);

        if (userOptional.isPresent()) {
            User user = userOptional.get();

            // Update user fields
            user.setUsername(userDTO.getUser_name());
            user.setEmail(userDTO.getEmail());
            user.setPassword(userDTO.getPassword());

            // Save the updated user in the database
            User updatedUser = userRepository.save(user);

            // Convert the updated entity to DTO and return it
            return Optional.of(entityToDto(updatedUser));
        } else {

            // Return empty Optional if user does not exist
            return Optional.empty();
        }
    }

    /**
     * Retrieves a list of all users from the database.
     *
     * @return A list of {@link UserDTO} representing all registered users.
     */
    public List<UserDTO> getAllUser() {

        // Fetch all users from the database
        List<User> users = (List<User>) userRepository.findAll();

        // Initialize an empty list to store user DTOs
        List<UserDTO> userDTOS = new ArrayList<>();

        // Convert each User entity to a UserDTO
        for (User user : users) {
            userDTOS.add(entityToDto(user));
        }

        // Return the list of user DTOs
        return userDTOS;
    }

    /**
     * Retrieves a user by their unique identifier.
     *
     * @param id The ID of the user to retrieve.
     * @return An {@link Optional} containing the {@link UserDTO} if the user exists, or an empty {@link Optional} if not found.
     */
    public Optional<UserDTO> getUserById(Long id) {

        // Fetch the user entity by ID from the repository
        Optional<User> user = userRepository.findById(id);

        // If the user exists, convert it to a DTO and return it
        if (user.isPresent()) {
            UserDTO userDTO = entityToDto(user.get());
            return Optional.of(userDTO);
        } else {
            // Return an empty Optional if the user does not exist
            return Optional.empty();
        }
    }

    /**
     * Retrieves a user by their email address.
     *
     * @param emailOrUsername The email of the user to retrieve.
     * @return An {@link Optional} containing the {@link UserDTO} if the user exists, or an empty {@link Optional} if not found.
     */
    public Optional<UserDTO> getUserByEmailOrUsername(String emailOrUsername) {

        Optional<User> user = userRepository.findByEmail(emailOrUsername);

        if (user.isEmpty()) {
            user = userRepository.findByUsername(emailOrUsername); // <-- corrigé ici
        }

        if (user.isPresent()) {
            System.out.println("Utilisateur trouvé : " + user.get());
        } else {
            System.out.println("Aucun utilisateur trouvé pour l'identifiant : " + emailOrUsername);
        }

        return user.map(this::entityToDto);

    }

    /**
     * Saves a new user to the database after encoding their password.
     *
     * @param userDTO The {@link UserDTO} containing the details of the user to be saved.
     * @return A {@link UserDTO} representing the saved user with their ID and other details.
     */
    public UserDTO saveUser(UserDTO userDTO) {

        // Convert the UserDTO to a User entity
        User user = dtoToEntity(userDTO);

        // Encode the user's password before saving it
        user.setPassword(passwordEncoder.encode(userDTO.getPassword()));

        // Save the user entity to the repository
        User savedUser = userRepository.save(user);

        // Return the saved user as a DTO
        return entityToDto(savedUser);
    }

    /**
     * Deletes a user from the database by their ID.
     *
     * @param id The ID of the user to be deleted.
     * @return An empty {@link Optional} as the return value since the operation is a delete.
     */
    public Optional<UserDTO> deleteUser(Long id) {
        // Deletes the user from the repository by their ID
        userRepository.deleteById(id);
        // Returns an empty Optional as there's no content to return after deletion
        return Optional.empty();
    }

    /**
     * Converts a {@link User} entity to a {@link UserDTO}.
     *
     * @param user The {@link User} entity to be converted.
     * @return A {@link UserDTO} containing the same information as the provided {@link User} entity.
     */
    private UserDTO entityToDto(User user) {

        UserDTO userDTO = new UserDTO();

        userDTO.setId(user.getId());
        userDTO.setUser_name(user.getUsername());
        userDTO.setEmail(user.getEmail());
        userDTO.setPassword(user.getPassword());

        return userDTO;
    }

    /**
     * Converts a {@link UserDTO} to a {@link User} entity.
     *
     * @param userDTO The {@link UserDTO} to be converted.
     * @return A {@link User} entity containing the same information as the provided {@link UserDTO}.
     */
    private User dtoToEntity(UserDTO userDTO) {

        User user = new User();

        user.setId(userDTO.getId());
        user.setUsername(userDTO.getUser_name());
        user.setEmail(userDTO.getEmail());
        user.setPassword(userDTO.getPassword());

        return user;
    }
}
