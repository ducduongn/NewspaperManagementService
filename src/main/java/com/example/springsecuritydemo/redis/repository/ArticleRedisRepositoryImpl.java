package com.example.springsecuritydemo.redis.repository;

import com.example.springsecuritydemo.models.articles.Article;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;
import java.util.Map;

@Repository
public class ArticleRedisRepositoryImpl implements ArticleRedisRepository {
    private final String hashReference = "Article";

    @Resource(name = "redisTemplate")
    private HashOperations<String, Long, Article> hashOperations;

    @Override
    public void saveArticle(Article article) {
        hashOperations.putIfAbsent(hashReference, article.getId(), article);
    }

    @Override
    public Article findArticleById(Long id) {
        return hashOperations.get(hashReference, id);
    }

    @Override
    public void updateArticle(Article article) {
        hashOperations.put(hashReference, article.getId(), article);
    }

    @Override
    public void deleteArticle(Long id) {
        hashOperations.delete(hashReference, id);
    }

    @Override
    public Map<Long, Article> getAllArticles() {
        return hashOperations.entries(hashReference);
    }

    @Override
    public void saveAllArticle(Map<Long, Article> map) {
        hashOperations.putAll(hashReference, map);
    }
}
