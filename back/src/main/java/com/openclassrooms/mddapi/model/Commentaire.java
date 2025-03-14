package com.openclassrooms.mddapi.model;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "COMMENTAIRES")
public class Commentaire {
@Id
@GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Integer auteur_id;
    private Integer article_id;
    private String commentary;
    @Column(name = "created_date", nullable = false)
    private LocalDateTime created_date;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getAuteur_id() {
        return auteur_id;
    }

    public void setAuteur_id(Integer auteur_id) {
        this.auteur_id = auteur_id;
    }

    public Integer getArticle_id() {
        return article_id;
    }

    public void setArticle_id(Integer article_id) {
        this.article_id = article_id;
    }

    public String getCommentary() {
        return commentary;
    }

    public void setCommentary(String commentary) {
        this.commentary = commentary;
    }

    public LocalDateTime getCreated_date() {
        return created_date;
    }

    public void setCreated_date(LocalDateTime created_date) {
        this.created_date = created_date;
    }
}
