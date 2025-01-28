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

    public  ThemeService(ThemeRepository themeRepository){
        this.themeRepository=themeRepository;
    }

    public List<ThemeDTO> getAllTheme() {

        List<Theme> themes = (List<Theme>) themeRepository.findAll();

        List<ThemeDTO> themeDTOS = new ArrayList<>();

        for (Theme theme : themes) {
            themeDTOS.add(entityToDto(theme));
        }

        return themeDTOS;
    }

    public Optional<ThemeDTO> getThemeById(Long id){
        // Fetch the rental entity by its ID from the repository
        Optional<Theme> theme = themeRepository.findById(id);
        // If the rental exists, convert it to a DTO and return it
        if (theme.isPresent()) {
            ThemeDTO themeDTO = entityToDto(theme.get());
            return Optional.of(themeDTO);
        } else {
            return Optional.empty();
        }
    }
    public ThemeDTO createTheme(ThemeDTO dto) {

        Theme theme = new Theme();
        theme.setId(dto.getId());
        theme.setName_theme(dto.getName_theme());

        Theme savedTheme = themeRepository.save(theme);


        ThemeDTO themeDTO = new ThemeDTO();
        themeDTO.setId(savedTheme.getId());
        themeDTO.setName_theme(savedTheme.getName_theme());


        return themeDTO;
    }

    private ThemeDTO entityToDto(Theme theme) {

        ThemeDTO themeDTO = new ThemeDTO();

        themeDTO.setId(theme.getId());
        themeDTO.setName_theme(theme.getName_theme());

        return themeDTO;
    }



    private Theme dtoToEntity(ThemeDTO themeDTO) {

        Theme theme = new Theme();

        theme.setId(themeDTO.getId());
        theme.setName_theme(themeDTO.getName_theme());

        return theme;
    }
}
