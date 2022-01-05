package com.example.springsecuritydemo.service.impl;

import com.example.springsecuritydemo.models.articles.Article;
import com.example.springsecuritydemo.models.dto.ArticleUpdateDto;
import com.example.springsecuritydemo.repository.ArticleRepository;
import com.example.springsecuritydemo.service.ArticleService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

/**
 * @author ducduongn
 */
@Service
@Slf4j
public class ArticleServiceImpl implements ArticleService {
    private ArticleRepository articleRepository;

    @Autowired
    public void setArticleRepository(ArticleRepository articleRepository) {
        this.articleRepository = articleRepository;
    }

    @Override
    public List<Article> findAll() {
        return articleRepository.findAll();
    }

    @Override
    public Article findById(Long id) {
        return articleRepository.findById(id)
                .orElse(null);
    }

    @Override
    public Article findByUrl(String url) {
        return articleRepository.findByUrl(url)
                .orElse(null);
    }

    @Override
    public Article updateArticleById(Long id, ArticleUpdateDto articleUpdateDto) {
        if (articleRepository.existsById(id)) {
            Article existingArticle = articleRepository.findById(id).get();

            existingArticle.setAuthor(articleUpdateDto.getAuthor());
            existingArticle.setDescription(articleUpdateDto.getDescription());
            existingArticle.setContent(articleUpdateDto.getContent());
            existingArticle.setTitle(articleUpdateDto.getTitle());

            Article updatedArticle = articleRepository.save(existingArticle);

            return updatedArticle;
        }
        return null;
    }

    @Override
    public Article updateArticleByUrl(String url, ArticleUpdateDto articleUpdateDto) {
        if (articleRepository.existsByUrl(url)) {
            Article existingArticle = articleRepository.findByUrl(url).get();

            log.info("Article found: " + existingArticle);

            existingArticle.setAuthor(articleUpdateDto.getAuthor());
            existingArticle.setDescription(articleUpdateDto.getDescription());
            existingArticle.setContent(articleUpdateDto.getContent());
            existingArticle.setTitle(articleUpdateDto.getTitle());
            existingArticle.setUpdatedDate(LocalDateTime.now());

            Article updatedArticle = articleRepository.save(existingArticle);

            return updatedArticle;
        }
        return null;
    }
}
