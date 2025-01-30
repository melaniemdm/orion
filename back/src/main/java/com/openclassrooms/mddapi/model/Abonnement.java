package com.openclassrooms.mddapi.model;

import javax.persistence.*;

@Entity
@Table(name = "ABONNEMENT_THEME")
public class Abonnement {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "auteur_id", nullable = false)
    private Integer user_id;
    @Column(name = "theme_id", nullable = true)
    private Integer theme_id;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getUser_id() {
        return user_id;
    }

    public void setUser_id(Integer user_id) {
        this.user_id = user_id;
    }

    public Integer getTheme_id() {
        return theme_id;
    }

    public void setTheme_id(Integer theme_id) {
        this.theme_id = theme_id;
    }
}
