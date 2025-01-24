package com.openclassrooms.mddapi.dto;


import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.LocalDateTime;

public class ArticleDTO {
    private Long id;
    @JsonProperty("description")
    private String description;
    @JsonProperty("auteur_id")
    private Integer auteur_id;
    @JsonProperty("create_date")
    private LocalDateTime create_date;
    @JsonProperty("create_date")
    private LocalDateTime updated_date;
    @JsonProperty("theme_date")
    private Integer theme_id;
    @JsonProperty("title")
    private String title;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Integer getAuteur_id() {
        return auteur_id;
    }

    public void setAuteur_id(Integer auteur_id) {
        this.auteur_id = auteur_id;
    }

    public LocalDateTime getCreate_date() {
        return create_date;
    }

    public void setCreate_date(LocalDateTime create_date) {
        this.create_date = create_date;
    }

    public LocalDateTime getUpdated_date() {
        return updated_date;
    }

    public void setUpdated_date(LocalDateTime updated_date) {
        this.updated_date = updated_date;
    }

    public Integer getTheme_id() {
        return theme_id;
    }

    public void setTheme_id(Integer theme_id) {
        this.theme_id = theme_id;
    }





}
