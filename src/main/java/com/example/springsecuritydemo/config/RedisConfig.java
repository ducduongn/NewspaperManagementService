package com.example.springsecuritydemo.config;

import com.example.springsecuritydemo.models.articles.Article;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.repository.configuration.EnableRedisRepositories;

/**
 * @author ducduongn
 */
@EnableRedisRepositories(basePackages = "com.example.springsecuritydemo.redis.repository")
@ComponentScan(basePackages = "com.example.springsecuritydemo")
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
