package com.openclassrooms.mddapi.service;

import com.openclassrooms.mddapi.dto.ArticleDTO;
import com.openclassrooms.mddapi.model.Article;
import com.openclassrooms.mddapi.repository.ArticleRepository;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class ArticleService {
    @Autowired
 private ArticleRepository articleRepository;

    public List<ArticleDTO> getAllArticle(){
        List<Article> articles=(List<Article>) articleRepository.findAll();
        List<ArticleDTO> articleDTOS =  new ArrayList<>();
        for(Article article: articles){
            articleDTOS.add(entityToDto(article));
        }
        return articleDTOS;
    }
    public void updateArticle(ArticleDTO articleDTO){
        articleRepository.save(dtoToEntity(articleDTO));
    }

    public void createArticle(ArticleDTO articleDTO) throws IOException {
       Article article = dtoToEntity(articleDTO);
       article.setCreate_date(LocalDateTime.now());
       article.setUpdated_at(LocalDateTime.now());
       articleRepository.save(article);
    }


    public Optional<ArticleDTO> getArticleById(Long id){
        Optional<Article> article = articleRepository.findById(id);
        if (article.isPresent()){
            ArticleDTO articleDTO = entityToDto(article.get());
            return Optional.of(articleDTO);
        }else {
            return Optional.empty();
        }

    }


    public void  deleteArticle(Long id){
         articleRepository.deleteById(id);
    }
    public Optional<ArticleDTO> updateArticle(Long id, ArticleDTO articleDTO){
        Optional<Article> articleOptional = articleRepository.findById(id);
        if (articleOptional.isPresent()){
            Article article = articleOptional.get();
            article.setId(articleDTO.getId());
            article.setTitle(articleDTO.getTitle());
            article.setDescription(articleDTO.getDescription());
            article.setAuteur_id(articleDTO.getAuteur_id());
            article.setTheme_id(articleDTO.getTheme_id());
            Article updatedArticle = articleRepository.save(article);
            return Optional.of(entityToDto(updatedArticle));
        }else{
            return Optional.empty();
        }
    }

    public ArticleDTO entityToDto(Article article) {

        ArticleDTO ArticleDTO = new ArticleDTO();

        ArticleDTO.setId(article.getId());
        ArticleDTO.setTheme_id(article.getTheme_id());
        ArticleDTO.setDescription(article.getDescription());
        ArticleDTO.setCreate_date(article.getCreate_date());
        ArticleDTO.setUpdated_date(article.getUpdated_at());
        ArticleDTO.setTitle(article.getTitle());
        ArticleDTO.setAuteur_id(article.getAuteur_id());

        return ArticleDTO;
    }

    // Converts a Data Transfer Object (DTO) to an entity
public Article dtoToEntity(ArticleDTO articleDTO) {

        Article article = new Article();

        article.setId(articleDTO.getId());
        article.setTheme_id(articleDTO.getTheme_id());
        article.setDescription(articleDTO.getDescription());
        article.setCreate_date(articleDTO.getCreate_date());
        article.setUpdated_at(articleDTO.getUpdated_date());
        article.setTitle(articleDTO.getTitle());
        article.setTitle(articleDTO.getTitle());

        return article;
    }


}
