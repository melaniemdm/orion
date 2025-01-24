package com.openclassrooms.mddapi.model;

import javax.persistence.*;

@Entity
@Table(name = "THEME")
public class Theme {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
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
