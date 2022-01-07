package com.example.springsecuritydemo.redis.service;

import com.example.springsecuritydemo.models.articles.Article;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface ArticleRedisService {
    Article addArticle(Article article);

    List<Article> findAll();

    Article findByArticleId(Long id);
}
