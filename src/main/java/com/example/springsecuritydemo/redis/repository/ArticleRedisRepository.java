package com.example.springsecuritydemo.redis.repository;

import com.example.springsecuritydemo.models.articles.Article;
import org.springframework.stereotype.Repository;

import java.util.Map;

@Repository
public interface ArticleRedisRepository {
    void saveArticle(Article article);

    Article findArticleById(Long id);

    void updateArticle(Article article);

    void deleteArticle(Long articleId);

    Map<Long, Article> getAllArticles();

    void saveAllArticle(Map<Long, Article> map);
}
