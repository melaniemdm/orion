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
@RequestMapping("post")
public class ArticleController {
@Autowired
private ArticleService articleService;

    public ArticleController(ArticleService articleService) {
        this.articleService = articleService;
    }


    @GetMapping
    public ResponseEntity<Map<String, List<ArticleDTO>>> getAllArticle() {
        List<ArticleDTO> articleDTOS = articleService.getAllArticle();

        return ResponseEntity.ok(Map.of("post", articleDTOS));
    }

    @PostMapping
    public ResponseEntity<Map<String, ArticleDTO>> createArticle(@RequestBody ArticleDTO articleDTO) {
        ArticleDTO createdDTO = articleService.createArticle(articleDTO);

        return new ResponseEntity<>(Map.of("post", createdDTO), HttpStatus.CREATED);
    }



    @GetMapping("/{id}")
    public ResponseEntity<Map<String, ArticleDTO>> getArticleById(@PathVariable Long id) {
        Optional<ArticleDTO> articleOpt = articleService.getArticleById(id);

        if (articleOpt.isPresent()) {
            // On renvoie {"article": articleDTO}
            return ResponseEntity.ok(Map.of("post", articleOpt.get()));
        } else {
            return ResponseEntity.notFound().build();
        }
    }


    @PutMapping("/{id}")
    public ResponseEntity<Map<String, ArticleDTO>> updateArticle(
            @PathVariable Long id,
            @RequestBody ArticleDTO articleDTO
    ) {
        Optional<ArticleDTO> updatedOpt = articleService.updateArticle(id, articleDTO);

        if (updatedOpt.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(Map.of("post", updatedOpt.get()));
    }


    @DeleteMapping("/{id}")
    public ResponseEntity<Map<String, String>> deleteArticle(@PathVariable Long id){
        articleService.deleteArticle(id);

        return ResponseEntity.ok(Map.of("message", "article deleted"));
    }
}
