package com.example.springsecuritydemo.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

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
