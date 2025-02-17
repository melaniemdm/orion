package com.openclassrooms.mddapi.service;


import com.openclassrooms.mddapi.dto.ThemeDTO;
import com.openclassrooms.mddapi.model.Theme;
import com.openclassrooms.mddapi.repository.ThemeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class ThemeService {
    @Autowired
    private ThemeRepository themeRepository;

    /**
     * Constructor for ThemeService to inject the ThemeRepository.
     * <p>
     * This constructor initializes the ThemeService with the provided {@link ThemeRepository}
     * which is used to interact with the theme data in the database.
     *
     * @param themeRepository The repository used to fetch and persist theme data in the database.
     */
    public ThemeService(ThemeRepository themeRepository) {
        this.themeRepository = themeRepository;
    }

    /**
     * Retrieves a list of all themes from the database and converts them into a list of DTOs.
     * <p>
     * This method fetches all the themes available in the database, converts each theme into a {@link ThemeDTO},
     * and returns a list of these DTOs.
     *
     * @return A list of {@link ThemeDTO} representing all themes available in the database.
     */
    public List<ThemeDTO> getAllTheme() {

        // Retrieve all themes from the database
        List<Theme> themes = (List<Theme>) themeRepository.findAll();

        // Create an empty list to store the theme DTOs
        List<ThemeDTO> themeDTOS = new ArrayList<>();

        // Iterate through each theme and convert it to a ThemeDTO
        for (Theme theme : themes) {
            // Convert each Theme entity to a ThemeDTO
            themeDTOS.add(entityToDto(theme));
        }
        // Return the list of ThemeDTOs
        return themeDTOS;
    }

    /**
     * Retrieves a theme by its ID from the database and converts it into a DTO.
     * <p>
     * This method looks for a theme in the database by its ID. If found, it converts the theme entity into a {@link ThemeDTO}
     * and returns it. If no theme is found with the provided ID, it returns an empty Optional.
     *
     * @param id The ID of the theme to retrieve.
     * @return An {@link Optional} containing the {@link ThemeDTO} if the theme exists, or an empty {@link Optional} if the theme does not exist.
     */
    public Optional<ThemeDTO> getThemeById(Long id) {
        // Fetch the rental entity by its ID from the repository
        Optional<Theme> theme = themeRepository.findById(id);
        // If the theme exists, convert it to a ThemeDTO and return it
        if (theme.isPresent()) {
            // Convert the theme entity to a DTO
            ThemeDTO themeDTO = entityToDto(theme.get());
            return Optional.of(themeDTO);
        } else {
            // If no theme is found, return an empty Optional
            return Optional.empty();
        }
    }

    /**
     * Creates a new theme by saving the provided {@link ThemeDTO} into the database.
     * <p>
     * This method converts the provided {@link ThemeDTO} into a {@link Theme} entity, saves it to the repository,
     * and then converts the saved entity back into a {@link ThemeDTO} to return to the client.
     *
     * @param dto The {@link ThemeDTO} containing the details of the theme to be created.
     * @return The created {@link ThemeDTO} containing the generated ID and the other details of the theme.
     */
    public ThemeDTO createTheme(ThemeDTO dto) {

        // Convert the ThemeDTO into a Theme entity
        Theme theme = new Theme();
        theme.setId(dto.getId());
        theme.setName_theme(dto.getName_theme());

        // Save the theme entity into the database
        Theme savedTheme = themeRepository.save(theme);

        // Convert the saved Theme entity back into a ThemeDTO
        ThemeDTO themeDTO = new ThemeDTO();
        themeDTO.setId(savedTheme.getId());
        themeDTO.setName_theme(savedTheme.getName_theme());

        // Return the ThemeDTO with the saved theme's details
        return themeDTO;
    }

    /**
     * Converts a {@link Theme} entity into a {@link ThemeDTO}.
     * <p>
     * This method takes a {@link Theme} entity as input, extracts its properties,
     * and maps them to a new {@link ThemeDTO} to be returned.
     *
     * @param theme The {@link Theme} entity to be converted.
     * @return A {@link ThemeDTO} containing the data from the provided {@link Theme} entity.
     */
    private ThemeDTO entityToDto(Theme theme) {

        // Create a new ThemeDTO object
        ThemeDTO themeDTO = new ThemeDTO();

        // Set the properties of the DTO from the entity
        themeDTO.setId(theme.getId());
        themeDTO.setName_theme(theme.getName_theme());

        // Return the populated DTO
        return themeDTO;
    }

    /**
     * Converts a {@link ThemeDTO} into a {@link Theme} entity.
     * <p>
     * This method takes a {@link ThemeDTO} as input, extracts its properties,
     * and maps them to a new {@link Theme} entity to be returned.
     *
     * @param themeDTO The {@link ThemeDTO} to be converted.
     * @return A {@link Theme} entity containing the data from the provided {@link ThemeDTO}.
     */
    private Theme dtoToEntity(ThemeDTO themeDTO) {
        // Create a new Theme entity object
        Theme theme = new Theme();

        // Set the properties of the entity from the DTO
        theme.setId(themeDTO.getId());
        theme.setName_theme(themeDTO.getName_theme());

        // Return the populated Theme entity
        return theme;
    }
}
