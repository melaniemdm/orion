package com.openclassrooms.mddapi.controller;

import com.openclassrooms.mddapi.model.Article;
import com.openclassrooms.mddapi.service.ArticleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("article")
public class ArticleController {
  @Autowired
  private ArticleService articleService;
    //Constructor
    public ArticleController(ArticleService articleService){
        this.articleService = articleService;
    }

    @GetMapping
    public ResponseEntity <List<Article>> getAllArticle(){
       return new ResponseEntity<>(articleService.getAllArticle(), HttpStatus.OK);
    }
    @PostMapping
    public ResponseEntity<Article> createArticle(@RequestBody Article article){
        Article articleCreated = articleService.createArticle(article);
        return new ResponseEntity<>(articleCreated, HttpStatus.CREATED);
    }
    @GetMapping("/{id}")
    public ResponseEntity<Article>getArticleById(@PathVariable Long id){
        Optional <Article> article = articleService.getArticleById(id);
        if(article.isPresent()){
            return new ResponseEntity<>(article.get(), HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }
    @PutMapping("/{id}")
    public ResponseEntity <Article> updateArticle(@PathVariable Long id, @RequestBody Article articleDetail){
        Optional <Article> article = articleService.updateArticle(id);
        if(article.isPresent()){
            Article existingArticle = article.get();
            existingArticle.setAuteur_id(articleDetail.getAuteur_id());
            existingArticle.setCreate_date(articleDetail.getCreate_date());
            existingArticle.setTitle(articleDetail.getTitle());
            existingArticle.setDescription(articleDetail.getDescription());
            existingArticle.setTheme_id(articleDetail.getTheme_id());

            Article updateArticle=articleService.updateArticle(existingArticle);
            return new ResponseEntity<>(HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteArticle(@PathVariable Long id){
        Optional <Article> article = articleService.deleteArticle(id);
    if (article.isPresent()){
        articleService.deleteArticle(article.get().getId());
        return new ResponseEntity<>(HttpStatus.OK);
    }
    return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }
}
