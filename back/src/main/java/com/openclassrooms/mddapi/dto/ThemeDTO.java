package com.openclassrooms.mddapi.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public class ThemeDTO {
    private Long id;
    @JsonProperty("name_theme")
    private String name_theme;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName_theme() {
        return name_theme;
    }

    public void setName_theme(String name_theme) {
        this.name_theme = name_theme;
    }
}
