package com.openclassrooms.mddapi.controller;

import com.openclassrooms.mddapi.dto.ArticleDTO;

import com.openclassrooms.mddapi.service.ArticleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
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
    public Map<String,List<ArticleDTO>> getAllArticle(){
        List<ArticleDTO> articleDTOS = articleService.getAllArticle();
       return Map.of("articles", articleDTOS);
    }

    @PostMapping
    public ResponseEntity<Map<String, Object>> createArticle(
            @RequestParam("title") String title,
            @RequestParam("description") String description,
            @RequestParam("auteur_id") Integer auteur_id,
            @RequestParam("theme_id") Integer theme_id,
            HttpServletRequest request){
        ArticleDTO articleDTO = new ArticleDTO();
        articleDTO.setTitle(title);
        articleDTO.setDescription(description);
        articleDTO.setAuteur_id(auteur_id);
        articleDTO.setTheme_id(theme_id);
        return  ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of("message", "failed to create article"));
    }
    @GetMapping("/{id}")
    public ResponseEntity getArticleById(@PathVariable Long id){
        Optional<ArticleDTO> articleDTO = articleService.getArticleById(id);
        if(articleDTO.isPresent()){
            return ResponseEntity.ok(articleDTO.get());
        }
        return ResponseEntity.notFound().build();
    }
    @PutMapping("/{id}")
    public ResponseEntity<Map<String, Object>> updateArticle(@PathVariable Long id,
                                                             @RequestParam("title") String title,
                                                             @RequestParam("description") String description,
                                                             @RequestParam("auteur_id") Integer auteur_id,
                                                             @RequestParam("theme_id") Integer theme_id,
                                                             HttpServletRequest request){

        ArticleDTO articleDTO = new ArticleDTO();
        articleDTO.setId(id);
        articleDTO.setTitle(title);
        articleDTO.setDescription(description);
        articleDTO.setAuteur_id(auteur_id);
        articleDTO.setTheme_id(theme_id);

        Optional <ArticleDTO> updateArticle = articleService.updateArticle(id, articleDTO);
        if(updateArticle.isPresent()){
           Map<String, Object> response = new HashMap<>();
           response.put("message", "article updated");
            return ResponseEntity.ok(response);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteArticle(@PathVariable Long id){
        articleService.deleteArticle(id);

    return ResponseEntity.ok("article delete");
    }
}
