package com.example.springsecuritydemo.config;

import com.example.springsecuritydemo.models.articles.Article;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;

/**
 * @author ducduongn
 */
@Configuration
public class RedisConfig {
    @Bean
    public RedisConnectionFactory connectionFactory() {
        return new LettuceConnectionFactory();
    }

    @Bean
    public RedisTemplate<String, Article> redisTemplate() {
        RedisTemplate<String, Article> articleRedisTemplate = new RedisTemplate<>();
        articleRedisTemplate.setConnectionFactory(connectionFactory());
        return articleRedisTemplate;
    }
}
