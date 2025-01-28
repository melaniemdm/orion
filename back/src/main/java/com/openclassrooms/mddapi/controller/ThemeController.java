package com.openclassrooms.mddapi.controller;


import com.openclassrooms.mddapi.dto.ThemeDTO;
import com.openclassrooms.mddapi.service.ThemeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("subject")
public class ThemeController {
    @Autowired
    private ThemeService themeService;

     //constructor
    public ThemeController(ThemeService themeService){
        this.themeService=themeService;
    }

    @GetMapping
    public ResponseEntity<Map<String, List<ThemeDTO>>> getAllTheme() {
        List<ThemeDTO> themeDTOS = themeService.getAllTheme();

        return ResponseEntity.ok(Map.of("subject", themeDTOS));
    }

    @PostMapping
    public ResponseEntity<Map<String, ThemeDTO>> createTheme(@RequestBody ThemeDTO themeDTO) {
        ThemeDTO createdDTO = themeService.createTheme(themeDTO);

        return new ResponseEntity<>(Map.of("subject", createdDTO), HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Map<String, ThemeDTO>> getThemeById(@PathVariable Long id) {
        Optional<ThemeDTO> themeOpt = themeService.getThemeById(id);

        if (themeOpt.isPresent()) {
            // On renvoie {"theme": themeDTO}
            return ResponseEntity.ok(Map.of("subject", themeOpt.get()));
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}
