package com.openclassrooms.mddapi.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public class CommentaireDTO {

    private Long id;
    @JsonProperty("auteur_id")
    private Integer auteur_id;
    @JsonProperty("article_id")
    private Integer article_id;
    @JsonProperty("commentary")
    private String commentary;

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
}
