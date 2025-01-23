package com.openclassrooms.mddapi.model;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "ARTICLES")
public class Article {
    @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;
    @Column(name = "title", nullable = false)
  private String title;
    @Column(name = "description", nullable = false)
  private String description;
    @Column(name = "auteur_id", nullable = false)
  private Integer auteur_id;
    @Column(name = "create_date", nullable = false)
  private LocalDateTime create_date;
    @Column(name = "updated_date", nullable = false)
    private LocalDateTime updated_date;
    @Column(name = "theme_id", nullable = false)
    private Integer theme_id;

    @PrePersist
    public void prePersist(){
        create_date = LocalDateTime.now();
        updated_date = LocalDateTime.now();
    }
    @PreUpdate
    public void preUpdate(){
        updated_date=LocalDateTime.now();
    }

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

    public LocalDateTime getUpdated_at() {
        return updated_date;
    }

    public void setUpdated_at(LocalDateTime updated_at) {
        this.updated_date = updated_at;
    }

    public Integer getTheme_id() {
        return theme_id;
    }

    public void setTheme_id(Integer theme_id) {
        this.theme_id = theme_id;
    }
}
