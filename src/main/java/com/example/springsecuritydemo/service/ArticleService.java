package com.example.springsecuritydemo.service;

import com.example.springsecuritydemo.models.articles.Article;
import com.example.springsecuritydemo.models.dto.ArticleUpdateDto;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author ducduongn
 */
@Service
public interface ArticleService {
    List<Article> findAll();

    Article findById(Long id);

    Article findByUrl(String url);

    Article updateArticleById(Long id, ArticleUpdateDto articleUpdateDto);

    Article updateArticleByUrl(String url, ArticleUpdateDto articleUpdateDto);
}
