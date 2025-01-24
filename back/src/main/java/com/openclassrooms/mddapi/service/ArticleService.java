package com.openclassrooms.mddapi.service;

import com.openclassrooms.mddapi.dto.ArticleDTO;
import com.openclassrooms.mddapi.model.Article;
import com.openclassrooms.mddapi.repository.ArticleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class ArticleService {
    @Autowired
    private ArticleRepository articleRepository;

    public  ArticleService(ArticleRepository articleRepository){
        this.articleRepository=articleRepository;
    }

    public ArticleDTO createArticle(ArticleDTO dto) {

        Article article = new Article();
        article.setTitle(dto.getTitle());
        article.setDescription(dto.getDescription());
        article.setAuteur_id(dto.getAuteur_id());
        article.setTheme_id(dto.getTheme_id());


        Article savedArticle = articleRepository.save(article);


        ArticleDTO articleDTO = new ArticleDTO();
        articleDTO.setId(savedArticle.getId());
        articleDTO.setTitle(savedArticle.getTitle());
        articleDTO.setDescription(savedArticle.getDescription());
        articleDTO.setAuteur_id(savedArticle.getAuteur_id());
        articleDTO.setTheme_id(savedArticle.getTheme_id());

        return articleDTO;
    }

    public Optional<ArticleDTO> updateArticle(Long id, ArticleDTO articleDTO) {

        Optional<Article> articleOptional = articleRepository.findById(id);

        if (articleOptional.isPresent()) {
            Article article = articleOptional.get();

            article.setTitle(articleDTO.getTitle());
            article.setDescription(articleDTO.getDescription());
            article.setAuteur_id(articleDTO.getAuteur_id());
            article.setTheme_id(articleDTO.getTheme_id());


            Article updatedArticle = articleRepository.save(article);

            return Optional.of(entityToDto(updatedArticle));
        } else {

            return Optional.empty();
        }
    }

    public List<ArticleDTO> getAllArticle() {

        List<Article> articles = (List<Article>) articleRepository.findAll();

        List<ArticleDTO> articleDTOs = new ArrayList<>();

        for (Article article : articles) {
            articleDTOs.add(entityToDto(article));
        }

        return articleDTOs;
    }

    public Optional<ArticleDTO> getArticleById(Long id){
        // Fetch the rental entity by its ID from the repository
        Optional<Article> article = articleRepository.findById(id);
        // If the rental exists, convert it to a DTO and return it
        if (article.isPresent()) {
            ArticleDTO articleDTO = entityToDto(article.get());
            return Optional.of(articleDTO);
        } else {
            return Optional.empty();
        }
    }
    public Optional<ArticleDTO> deleteArticle(Long id){
        articleRepository.deleteById(id);
        return Optional.empty();
    }

    private ArticleDTO entityToDto(Article article) {

        ArticleDTO articleDTO = new ArticleDTO();

        articleDTO.setId(article.getId());
        articleDTO.setTitle(article.getTitle());
        articleDTO.setDescription(article.getDescription());
        articleDTO.setAuteur_id(article.getAuteur_id());
        articleDTO.setTheme_id(article.getTheme_id());
        articleDTO.setCreated_date(article.getCreated_date());
        articleDTO.setUpdated_date(article.getUpdated_date());
        return articleDTO;
    }


    private Article dtoToEntity(ArticleDTO articleDTO) {

        Article article = new Article();

        article.setId(articleDTO.getId());
        article.setTitle(articleDTO.getTitle());
        article.setDescription(articleDTO.getDescription());
        article.setAuteur_id(articleDTO.getAuteur_id());
        article.setTheme_id(articleDTO.getTheme_id());
        article.setCreated_date(articleDTO.getCreated_date());
        article.setUpdated_date(articleDTO.getUpdated_date());
        return article;
    }


}
