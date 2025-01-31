package com.openclassrooms.mddapi.service;


import com.openclassrooms.mddapi.dto.UserDTO;
import com.openclassrooms.mddapi.model.User;
import com.openclassrooms.mddapi.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;

    public  UserService(UserRepository userRepository){
        this.userRepository=userRepository;
    }
    public UserDTO createUser(UserDTO dto) {

        User user = new User();
        user.setUser_name(dto.getUser_name());
        user.setEmail(dto.getEmail());
        user.setPassword(dto.getPassword());

        User savedUser = userRepository.save(user);

        UserDTO userDTO = new UserDTO();
        userDTO.setId(savedUser.getId());
        userDTO.setUser_name(savedUser.getUser_name());
        userDTO.setEmail(savedUser.getEmail());
        userDTO.setPassword(savedUser.getPassword());

        return userDTO;
    }

    public Optional<UserDTO> updateUser(Long id, UserDTO userDTO) {

        Optional<User> userOptional = userRepository.findById(id);

        if (userOptional.isPresent()) {
            User user = userOptional.get();

            user.setUser_name(userDTO.getUser_name());
            user.setEmail(userDTO.getEmail());
            user.setPassword(userDTO.getPassword());

            User updatedUser = userRepository.save(user);

            return Optional.of(entityToDto(updatedUser));
        } else {

            return Optional.empty();
        }
    }
    public List<UserDTO> getAllUser() {

        List<User> users = (List<User>) userRepository.findAll();

        List<UserDTO> userDTOS= new ArrayList<>();

        for (User user : users) {
            userDTOS.add(entityToDto(user));
        }

        return userDTOS;
    }
    public Optional<UserDTO> getUserById(Long id){

        Optional<User> user = userRepository.findById(id);

        if (user.isPresent()) {
            UserDTO userDTO = entityToDto(user.get());
            return Optional.of(userDTO);
        } else {
            return Optional.empty();
        }
    }

    public Optional<UserDTO> getUserByEmail(String email) {

        Optional<User> user = userRepository.findByEmail(email);

        if (user.isPresent()) {
            System.out.println("email : : " + user.get());
        } else {
            System.out.println("Aucun utilisateur trouvé pour login : " +  email);
        }
        // Convert the User entity to a UserDTO if present and return it
        return user.map(this::entityToDto);

    }

    public UserDTO saveUser(UserDTO userDTO) {

        User user = dtoToEntity(userDTO);

        user.setPassword(passwordEncoder.encode(userDTO.getPassword()));

        //user.setCreatedAt(LocalDateTime.now());
        //user.setUpdatedAt(LocalDateTime.now());

        User savedUser = userRepository.save(user);

        return entityToDto(savedUser);
    }


    public Optional<UserDTO> deleteUser(Long id){
        userRepository.deleteById(id);
        return Optional.empty();
    }

    private UserDTO entityToDto(User user) {

        UserDTO userDTO = new UserDTO();

        userDTO.setId(user.getId());
        userDTO.setUser_name(user.getUser_name());
        userDTO.setEmail(user.getEmail());
        userDTO.setPassword(user.getPassword());

        return userDTO;
    }


    private User dtoToEntity(UserDTO userDTO) {

        User user = new User();

        user.setId(userDTO.getId());
        user.setUser_name(userDTO.getUser_name());
        user.setEmail(userDTO.getEmail());
        user.setPassword(userDTO.getPassword());

        return user;
    }
}
