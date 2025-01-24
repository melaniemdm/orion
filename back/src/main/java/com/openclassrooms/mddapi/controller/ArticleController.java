package com.openclassrooms.mddapi.controller;

import com.openclassrooms.mddapi.dto.ArticleDTO;
import com.openclassrooms.mddapi.service.ArticleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("article")
public class ArticleController {
@Autowired
private ArticleService articleService;

    public ArticleController(ArticleService articleService) {
        this.articleService = articleService;
    }


    @GetMapping
    public ResponseEntity<Map<String, List<ArticleDTO>>> getAllArticle() {
        List<ArticleDTO> articleDTOS = articleService.getAllArticle();

        return ResponseEntity.ok(Map.of("article", articleDTOS));
    }

    @PostMapping
    public ResponseEntity<ArticleDTO> createArticle(@RequestBody ArticleDTO articleDTO) {

        ArticleDTO createdDTO = articleService.createArticle(articleDTO);

        return new ResponseEntity<>(createdDTO, HttpStatus.CREATED);
    }


    @GetMapping("/{id}")
    public ResponseEntity<ArticleDTO>getArticleById(@PathVariable Long id){
        Optional <ArticleDTO> article = articleService.getArticleById(id);
        if(article.isPresent()){
            return ResponseEntity.ok(article.get());
        }
        return ResponseEntity.notFound().build();
    }

    @PutMapping("/{id}")
    public ResponseEntity<Optional<ArticleDTO>> updateArticle(
            @PathVariable Long id,
            @RequestBody ArticleDTO articleDTO
    ) {
        // On passe bien deux paramètres : l’ID et le DTO
        Optional<ArticleDTO> updated = articleService.updateArticle(id, articleDTO);

        if (updated.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(updated);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Map<String, String>> deleteArticle(@PathVariable Long id){
        articleService.deleteArticle(id);

        return ResponseEntity.ok(Map.of("message", "article deleted"));
    }
}
