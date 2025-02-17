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
@RequestMapping("/subject")
public class ThemeController {
    @Autowired
    private ThemeService themeService;

    /**
     * Constructor for initializing the {@link ThemeController} with the required {@link ThemeService}.
     *
     * @param themeService The {@link ThemeService} used for handling the theme-related operations.
     */
    public ThemeController(ThemeService themeService) {
        this.themeService = themeService;
    }

    /**
     * Retrieves a list of all available themes.
     *
     * @return A {@link ResponseEntity} containing a map with the key "subject" and the value being a list of {@link ThemeDTO} representing all available themes.
     */
    @GetMapping
    public ResponseEntity<Map<String, List<ThemeDTO>>> getAllTheme() {
        // Fetch the list of all themes from the theme service
        List<ThemeDTO> themeDTOS = themeService.getAllTheme();

        // Return a ResponseEntity with a 200 OK status, including the list of themes in the response body.
        return ResponseEntity.ok(Map.of("subject", themeDTOS));
    }

    /**
     * Creates a new theme based on the provided data.
     *
     * @param themeDTO The {@link ThemeDTO} containing the details of the theme to be created.
     * @return A {@link ResponseEntity} containing a map with the key "subject" and the value being the created {@link ThemeDTO}.
     * The response will have a status code of 201 (CREATED) upon successful creation of the theme.
     */
    @PostMapping
    public ResponseEntity<Map<String, ThemeDTO>> createTheme(@RequestBody ThemeDTO themeDTO) {
        // Create the theme using the themeService, which interacts with the database.
        ThemeDTO createdDTO = themeService.createTheme(themeDTO);
        // Return a ResponseEntity with a 201 CREATED status, including the created theme in the response body.
        return new ResponseEntity<>(Map.of("subject", createdDTO), HttpStatus.CREATED);
    }

    /**
     * Retrieves a specific theme by its ID.
     * <p>
     * This method retrieves a theme from the database using the provided ID and returns it
     * as a {@link ThemeDTO}. If the theme is found, a 200 (OK) status is returned with the theme.
     * If the theme is not found, a 404 (Not Found) status is returned.
     *
     * @param id The ID of the theme to retrieve.
     * @return A {@link ResponseEntity} containing:
     * - A map with the key "subject" and the value being the {@link ThemeDTO} if the theme is found.
     * - A 404 (Not Found) status if the theme does not exist.
     */
    @GetMapping("/{id}")
    public ResponseEntity<Map<String, ThemeDTO>> getThemeById(@PathVariable Long id) {
        // Attempt to fetch the theme by ID using the theme service
        Optional<ThemeDTO> themeOpt = themeService.getThemeById(id);

        // Check if the theme was found
        if (themeOpt.isPresent()) {
            // Return the found theme with a 200 OK status
            return ResponseEntity.ok(Map.of("subject", themeOpt.get()));
        } else {
            // Return a 404 Not Found status if the theme does not exist
            return ResponseEntity.notFound().build();
        }
    }
}
