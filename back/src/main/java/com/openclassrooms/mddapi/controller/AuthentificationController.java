package com.openclassrooms.mddapi.controller;


import com.openclassrooms.mddapi.dto.UserDTO;
import com.openclassrooms.mddapi.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/auth")
public class AuthentificationController {
   @Autowired
   private UserService userService;
    @Autowired
    private PasswordEncoder passwordEncoder;

   //constructeur
    public AuthentificationController(UserService userService){
        this.userService = userService;
    }


    @PostMapping("/register")
    public ResponseEntity<?> createUser(@RequestBody UserDTO userDTO) {

        if (userDTO.getPassword() == null || userDTO.getPassword().trim().isEmpty()) {
            // If the password is missing, return a 400 Bad Request response
            return ResponseEntity.badRequest().body("Password is required");
        }
        UserDTO createdDTO = userService.saveUser(userDTO);

        return new ResponseEntity<>(Map.of("user", createdDTO), HttpStatus.CREATED);
    }

    @PostMapping("/login")
    public ResponseEntity<?> loginUser(@RequestBody UserDTO loginRequest) {

        Optional<UserDTO> optionalUser = userService.getUserByEmail(loginRequest.getEmail());

        if (optionalUser.isEmpty()) {

            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body("Invalid email or password.");
        }

        UserDTO userDTO = optionalUser.get();

System.out.println("userDTO.password() est : " + userDTO.getPassword());
System.out.println("loginRequest.password() est : " + loginRequest.getPassword());

        boolean isPasswordValid = passwordEncoder.matches(loginRequest.getPassword(), userDTO.getPassword());

        if (!isPasswordValid ) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body("Invalid email or password.");
        }
        return ResponseEntity.ok(userDTO);
    }
}
