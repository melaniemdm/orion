package com.openclassrooms.mddapi.controller;


import com.openclassrooms.mddapi.dto.UserDTO;
import com.openclassrooms.mddapi.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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

   //constructeur
    public AuthentificationController(UserService userService){
        this.userService = userService;
    }


    @PostMapping("/register")
    public ResponseEntity<Map<String, UserDTO>> createUser(@RequestBody UserDTO userDTO) {
        UserDTO createdDTO = userService.createUser(userDTO);

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
System.out.println("userDTO.email() est : " + userDTO.getEmail());
System.out.println("loginRequest.email() est : " + loginRequest.getEmail());
        if (!userDTO.getEmail().equals(loginRequest.getEmail())) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body("Invalid email or password.");
        }
        return ResponseEntity.ok(userDTO);
    }
}
