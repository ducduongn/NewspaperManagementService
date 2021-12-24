package com.example.springsecuritydemo.controller;

import com.example.springsecuritydemo.models.articles.Article;
import com.example.springsecuritydemo.service.article.ArticleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @author ducduongn
 */
@RestController
@RequestMapping("/api/v1/article")
public class ArticleController {
    private ArticleService articleService;

    @Autowired
    public void setArticleService(ArticleService articleService) {
        this.articleService = articleService;
    }

    @GetMapping("/all")
    public ResponseEntity<?> getAllArticles() {
        List<Article> articleList = articleService.findAll();

        return ResponseEntity.ok(articleList);
    }

    @GetMapping("/get")
    public ResponseEntity<?> getArticleByUrl(@RequestParam String articleUrl) {
        Article article = articleService.findByUrl(articleUrl);
        return ResponseEntity.ok(article);
    }
}
