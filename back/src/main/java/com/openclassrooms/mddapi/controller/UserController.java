package com.openclassrooms.mddapi.controller;

import com.openclassrooms.mddapi.model.User;
import com.openclassrooms.mddapi.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/user")
public class UserController {
    @Autowired
    UserRepository userRepository;

    //constructeur
    public UserController(UserRepository userRepository){
        this.userRepository = userRepository;
    }
    @GetMapping
    public ResponseEntity<List<User>> getAllPersons(){
        return new ResponseEntity<>(userRepository.findAll(), HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity <Void > userPerson(@PathVariable Long id){
        Optional <User> user = userRepository.findById(id);

        if(user.isPresent()){
            userRepository.delete(user.get());
            return new ResponseEntity<>(HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @PutMapping("/{id}")
    public ResponseEntity <User> userPerson(@PathVariable Long id, @RequestBody User userDetails){
        Optional <User> user = userRepository.findById(id);

        if(user.isPresent()){
            User  existingUser = user.get();
            existingUser.setUser_name(userDetails.getUser_name());
            existingUser.setEmail((userDetails.getEmail()));
            existingUser.setPassword((userDetails.getPassword()));

            User updatePerson=userRepository.save(existingUser);
            return new ResponseEntity<>(updatePerson, HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }
    @GetMapping("/{id}")
    public ResponseEntity <User> getUserById(@PathVariable Long id){
        Optional <User> user = userRepository.findById(id);

        if(user.isPresent()){
            return new ResponseEntity<>(user.get(),HttpStatus.OK);

        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }
}
