package com.example.springsecuritydemo.service.impl;

import com.example.springsecuritydemo.models.articles.Article;
import com.example.springsecuritydemo.repository.ArticleRepository;
import com.example.springsecuritydemo.service.ArticleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author ducduongn
 */
@Service
public class ArticleServiceImpl implements ArticleService {
    private ArticleRepository articleRepository;

    @Autowired
    public void setArticleRepository(ArticleRepository articleRepository) {
        this.articleRepository = articleRepository;
    }

    @Override
    public List<Article> findAll() {
        return articleRepository.findAll();
    }

    @Override
    public Article findById(Long id) {
        return articleRepository.findById(id)
                .orElse(null);
    }

    @Override
    public Article findByUrl(String url) {
        return articleRepository.findByUrl(url)
                .orElse(null);
    }
}
