package com.example.springsecuritydemo.service;

import com.example.springsecuritydemo.models.articles.Article;

import java.util.List;

public interface ArticleRedisService {
    Article addArticle(Article article);

    List<Article> findAll();

    Article findByArticleId(Long id);
}
