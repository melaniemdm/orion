package com.openclassrooms.mddapi.controller;

import com.openclassrooms.mddapi.model.Theme;
import com.openclassrooms.mddapi.repository.ThemeRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("theme")
public class ThemeController {
    final ThemeRepository themeRepository;
    //constructor
    public ThemeController(ThemeRepository themeRepository){
        this.themeRepository=themeRepository;
    }

    @GetMapping
    public ResponseEntity<List<Theme>> getAllTheme(){
        return new ResponseEntity<>(themeRepository.findAll(), HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<Theme> createTheme(@RequestBody Theme theme ){
        Theme personCreated = themeRepository.save(theme);
        return new ResponseEntity<>(personCreated, HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity <Theme> getThemeById(@PathVariable Long id){
        Optional<Theme> person = themeRepository.findById(id);

        if(person.isPresent()){
            return new ResponseEntity<>(person.get(),HttpStatus.OK);

        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }
}
