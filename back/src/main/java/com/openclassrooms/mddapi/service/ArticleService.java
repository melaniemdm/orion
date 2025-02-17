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

    /**
     * Constructor for the {@link ArticleService} class.
     *
     * @param articleRepository The repository used for accessing and managing article data.
     */
    public ArticleService(ArticleRepository articleRepository) {
        this.articleRepository = articleRepository;
    }

    /**
     * Creates a new article and saves it to the database.
     *
     * @param dto The {@link ArticleDTO} containing the details of the article to be created.
     * @return The newly created {@link ArticleDTO} with its generated ID.
     */
    public ArticleDTO createArticle(ArticleDTO dto) {
        // Create a new Article entity from the DTO
        Article article = new Article();
        article.setTitle(dto.getTitle());
        article.setDescription(dto.getDescription());
        article.setAuteur_id(dto.getAuteur_id());
        article.setTheme_id(dto.getTheme_id());

        // Save the article to the database
        Article savedArticle = articleRepository.save(article);

        // Convert the saved entity back to DTO
        ArticleDTO articleDTO = new ArticleDTO();
        articleDTO.setId(savedArticle.getId());
        articleDTO.setTitle(savedArticle.getTitle());
        articleDTO.setDescription(savedArticle.getDescription());
        articleDTO.setAuteur_id(savedArticle.getAuteur_id());
        articleDTO.setTheme_id(savedArticle.getTheme_id());

        return articleDTO;
    }

    /**
     * Updates an existing article in the database.
     *
     * @param id         The ID of the article to update.
     * @param articleDTO The {@link ArticleDTO} containing the new article details.
     * @return An {@link Optional} containing the updated {@link ArticleDTO} if the article exists,
     * otherwise an empty {@link Optional}.
     */
    public Optional<ArticleDTO> updateArticle(Long id, ArticleDTO articleDTO) {
        // Retrieve the article by ID
        Optional<Article> articleOptional = articleRepository.findById(id);

        if (articleOptional.isPresent()) {
            // Update the article with new details
            Article article = articleOptional.get();
            article.setTitle(articleDTO.getTitle());
            article.setDescription(articleDTO.getDescription());
            article.setAuteur_id(articleDTO.getAuteur_id());
            article.setTheme_id(articleDTO.getTheme_id());

            // Save the updated article to the database
            Article updatedArticle = articleRepository.save(article);

            // Convert and return the updated entity as DTO
            return Optional.of(entityToDto(updatedArticle));
        } else {
            // Return an empty Optional if the article does not exist
            return Optional.empty();
        }
    }

    /**
     * Retrieves a list of all articles from the database.
     *
     * @return A {@link List} of {@link ArticleDTO} representing all available articles.
     */
    public List<ArticleDTO> getAllArticle() {
        // Retrieve all articles from the repository
        List<Article> articles = (List<Article>) articleRepository.findAll();

        // Convert the list of Article entities to DTOs
        List<ArticleDTO> articleDTOs = new ArrayList<>();

        for (Article article : articles) {
            articleDTOs.add(entityToDto(article));
        }
        // Return the list of article DTOs
        return articleDTOs;
    }

    /**
     * Retrieves an article by its ID.
     *
     * @param id The ID of the article to retrieve.
     * @return An {@link Optional} containing the {@link ArticleDTO} if found, or an empty {@link Optional} if the article does not exist.
     */
    public Optional<ArticleDTO> getArticleById(Long id) {
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

    /**
     * Deletes an article by its ID.
     *
     * @param id The ID of the article to delete.
     * @return An empty {@link Optional} after deletion.
     */
    public Optional<ArticleDTO> deleteArticle(Long id) {
        articleRepository.deleteById(id);
        return Optional.empty();
    }

    /**
     * Converts an {@link Article} entity to a {@link ArticleDTO}.
     *
     * @param article The {@link Article} entity to convert.
     * @return An {@link ArticleDTO} containing the same information as the entity.
     */
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

    /**
     * Converts an {@link ArticleDTO} to an {@link Article} entity.
     *
     * @param articleDTO The {@link ArticleDTO} object to convert.
     * @return An {@link Article} entity containing the same information as the DTO.
     */
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
