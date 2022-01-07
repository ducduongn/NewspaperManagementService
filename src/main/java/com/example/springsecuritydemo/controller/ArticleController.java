package com.example.springsecuritydemo.controller;

import auth.payload.MessageResponse;
import com.example.springsecuritydemo.models.articles.Article;
import com.example.springsecuritydemo.models.dto.ArticleUpdateDto;
import com.example.springsecuritydemo.redis.service.ArticleRedisService;
import com.example.springsecuritydemo.service.ArticleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author ducduongn
 */
@RestController
@RequestMapping("/api/v1/article")
public class ArticleController {
    @Autowired
    private ArticleService articleService;

    @Autowired
    private ArticleRedisService articleRedisService;

    @PreAuthorize("hasRole('ROLE_JOURNALIST') or " +
            "hasRole('ROLE_EDITOR') or " +
            "hasRole('_ROLE_DIRECTOR') or " +
            "hasRole('ROLE_ADMIN')")
    @GetMapping("/all")
    public ResponseEntity<?> getAllArticles() {
        List<Article> articleList = articleService.findAll();

        return ResponseEntity.ok(articleList);
    }

    @PreAuthorize("hasRole('ROLE_JOURNALIST') or " +
            "hasRole('ROLE_EDITOR') or " +
            "hasRole('_ROLE_DIRECTOR') or " +
            "hasRole('ROLE_ADMIN')")
    @GetMapping("/get")
    public ResponseEntity<?> getArticleByUrl(@RequestParam String articleUrl) {
        Article article = articleService.findByUrl(articleUrl);
        if (article != null) {
            return ResponseEntity.ok(article);
        }
        return ResponseEntity.ok(new MessageResponse("Article not found!"));
    }

    @PutMapping(value = "/update/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<?> updateArticleById(@PathVariable(value = "id") Long id,
                                           @RequestBody ArticleUpdateDto articleUpdateDto) {
        return new ResponseEntity<>(articleService.updateArticleById(id, articleUpdateDto),
                HttpStatus.OK);
    }

    @PreAuthorize("hasRole('ROLE_EDITOR') or " +
            "hasRole('ROLE_ADMIN')")
    @PutMapping(value = "/",  produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<?> updateArticleByUrl(@RequestParam(name = "url") String url,
                                                @RequestBody ArticleUpdateDto articleUpdateDto) {
        Article article = articleService.updateArticleByUrl(url, articleUpdateDto);
        if (article != null) {
            return new ResponseEntity<>(articleService.updateArticleByUrl(url, articleUpdateDto),
                    HttpStatus.OK);
        }
        return ResponseEntity.ok(new MessageResponse("Update article fail!"));
    }

    @GetMapping("/get/{id}")
    public ResponseEntity<?> getArticleById(@PathVariable Long id) {
        Article article = articleRedisService.findByArticleId(id);

        if (article!= null) {
            return ResponseEntity.ok(article);
        }

        return ResponseEntity.ok(new MessageResponse("Article is not found"));
    }

}
