package com.example.springsecuritydemo.repository;

import com.example.springsecuritydemo.models.articles.Article;

import java.util.Map;

public interface TopArticleRedisRepository {
    void saveArticle(Article article);

    Article findArticleById(Long id);

    void updateArticle(Article article);

    void deleteArticle(Long articleId);

    Map<Long, Article> getAllArticles();
}
