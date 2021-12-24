package com.example.springsecuritydemo.service.article;

import com.example.springsecuritydemo.models.articles.Article;
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
}
