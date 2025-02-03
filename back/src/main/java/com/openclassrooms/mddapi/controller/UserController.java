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

    //constructeur
    public UserController(UserService userService){
        this.userService = userService;
    }
    @GetMapping
    public ResponseEntity<Map<String, List<UserDTO>>> getAllUser() {
        List<UserDTO> userDTOS = userService.getAllUser();

        return ResponseEntity.ok(Map.of("user", userDTOS));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Map<String, String>> deleteUser(@PathVariable Long id){
        userService.deleteUser(id);

        return ResponseEntity.ok(Map.of("user", "user deleted"));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Map<String, UserDTO>> updateUser(
            @PathVariable Long id,
            @RequestBody UserDTO userDTO
    ) {
        Optional<UserDTO> updatedOpt = userService.updateUser(id, userDTO);

        if (updatedOpt.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(Map.of("user", updatedOpt.get()));
    }

    @GetMapping("/{id}")
    public ResponseEntity<Map<String, String>> getUserById(@PathVariable Long id) {
        Optional<UserDTO> userOpt = userService.getUserById(id);
System.out.println("c'est ca :" + userOpt);
        if (userOpt.isPresent()) {

            return ResponseEntity.ok(Map.of("userName", userOpt.get().getUser_name()));
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}
