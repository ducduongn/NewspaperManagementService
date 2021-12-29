package com.example.springsecuritydemo.config;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.elasticsearch.repository.config.EnableElasticsearchRepositories;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

/**
 * @author ducduongn
 */
@Configuration
@EnableJpaRepositories(basePackages = "com.example.springsecuritydemo.repository")
@ComponentScan(basePackages = "com.example.springsecuritydemo")
public class JpaConfig {
}
