package com.example.springsecuritydemo.redis.repository;

import com.example.springsecuritydemo.models.articles.Article;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;
import java.util.Map;

@Repository
public class ArticleRedisRepositoryImpl implements ArticleRedisRepository {
    private final String recentViewArticle = "RecentViewArticle";

    @Resource(name = "redisTemplate")
    private HashOperations<String, Long, Article> hashOperations;

    @Override
    public void saveArticle(Article article) {
        hashOperations.putIfAbsent(recentViewArticle, article.getId(), article);
    }

    @Override
    public Article findArticleById(Long id) {
        return hashOperations.get(recentViewArticle, id);
    }

    @Override
    public void updateArticle(Article article) {
        hashOperations.put(recentViewArticle, article.getId(), article);
    }

    @Override
    public void deleteArticle(Long id) {
        hashOperations.delete(recentViewArticle, id);
    }

    @Override
    public Map<Long, Article> getAllArticles() {
        return hashOperations.entries(recentViewArticle);
    }

    @Override
    public void saveAllArticle(Map<Long, Article> map) {
        hashOperations.putAll(recentViewArticle, map);
    }
}
