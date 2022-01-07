package com.example.springsecuritydemo.service.impl;

import com.example.springsecuritydemo.models.articles.Article;
import com.example.springsecuritydemo.redis.repository.ArticleRedisRepository;
import com.example.springsecuritydemo.repository.ArticleRepository;
import com.example.springsecuritydemo.service.ArticleRedisService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ArticleRedisServiceImpl implements ArticleRedisService {

    @Autowired
    private ArticleRedisRepository articleRedisRepository;

    @Autowired
    private ArticleRepository articleRepository;


    @Override
    public Article addArticle(Article article) {
        return articleRepository.save(article);
    }

    @Override
    public List<Article> findAll() {
        return articleRepository.findAll();
    }

    @Override
    public Article findByArticleId(Long id) {
        Article cacheArticle = articleRedisRepository.findArticleById(id);

        if (cacheArticle == null) {
            Article dbArticle = articleRepository.findById(id).orElse(null);

            if (dbArticle != null)
                articleRedisRepository.saveArticle(dbArticle);

            return dbArticle;
        }
        return cacheArticle;
    }
}
