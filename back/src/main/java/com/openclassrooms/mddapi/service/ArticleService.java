package com.openclassrooms.mddapi.service;

import com.openclassrooms.mddapi.model.Article;
import com.openclassrooms.mddapi.repository.ArticleRepository;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.Optional;

public class ArticleService {
    @Autowired
 private ArticleRepository articleRepository;

    public Article createArticle(Article article){
      return articleRepository.save(article);
    }
    public Article updateArticle(Article article){
        return articleRepository.save(article);
    }
    public List<Article> getAllArticle(){
   return articleRepository.findAll();
    }
    public Optional<Article> getArticleById(Long id){
        return articleRepository.findById(id);
    }
    public Optional<Article> deleteArticle(Long id){
        return articleRepository.findById(id);
    }
    public Optional<Article> updateArticle(Long id){
        return articleRepository.findById(id);
    }
}
