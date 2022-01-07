package com.example.springsecuritydemo.redis.repository;


import com.example.springsecuritydemo.models.articles.Article;
import org.springframework.data.redis.core.HashOperations;

import javax.annotation.Resource;
import java.util.Map;

public class HotArticleRepository implements ArticleRedisRepository{

    private final String hotViewArticle = "HotViewArticle";

    @Resource(name = "redisTemplate")
    private HashOperations<String, Long, Article> hashOperations;

    @Override
    public void saveArticle(Article article) {
        hashOperations.putIfAbsent(hotViewArticle, article.getId(), article);
    }

    @Override
    public Article findArticleById(Long id) {
        return hashOperations.get(hotViewArticle, id);
    }

    @Override
    public void updateArticle(Article article) {
        hashOperations.put(hotViewArticle, article.getId(), article);
    }

    @Override
    public void deleteArticle(Long id) {
        hashOperations.delete(hotViewArticle, id);
    }

    @Override
    public Map<Long, Article> getAllArticles() {
        return hashOperations.entries(hotViewArticle);
    }

    @Override
    public void saveAllArticle(Map<Long, Article> map) {
        hashOperations.putAll(hotViewArticle, map);
    }
}
