package com.example.springsecuritydemo.controller;

import com.example.springsecuritydemo.models.articles.Article;
import com.example.springsecuritydemo.models.dto.ArticleUpdateDto;
import com.example.springsecuritydemo.service.ArticleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

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

    public ResponseEntity<?> getAllArticles() {
        List<Article> articleList = articleService.findAll();

        return ResponseEntity.ok(articleList);
    }

    @GetMapping("/get")
    public ResponseEntity<?> getArticleByUrl(@RequestParam String articleUrl) {
        Article article = articleService.findByUrl(articleUrl);
        return ResponseEntity.ok(article);
    }

    @PutMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<?> updateArticleById(@PathVariable(value = "id") Long id,
                                           @RequestBody ArticleUpdateDto articleUpdateDto) {
        return new ResponseEntity<>(articleService.updateArticleById(id, articleUpdateDto),
                HttpStatus.OK);
    }

    @PutMapping(value = "/",  produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<?> updateArticleByUrl(@RequestParam(name = "url") String url,
                                                @RequestBody ArticleUpdateDto articleUpdateDto) {
        return new ResponseEntity<>(articleService.updateArticleByUrl(url, articleUpdateDto),
                HttpStatus.OK);
    }

}
