package com.example.springsecuritydemo.redis.repository;

import com.example.springsecuritydemo.models.articles.Article;
import com.example.springsecuritydemo.redis.repository.TopArticleRedisRepository;
import org.springframework.data.redis.core.HashOperations;

import javax.annotation.Resource;
import java.util.Map;

public class TopArticleRedisRepositoryImpl implements TopArticleRedisRepository {
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
    public void deleteArticle(Long articleId) {
        hashOperations.delete(hashReference, articleId);
    }

    @Override
    public Map<Long, Article> getAllArticles() {
        return hashOperations.entries(hashReference);
    }
}
