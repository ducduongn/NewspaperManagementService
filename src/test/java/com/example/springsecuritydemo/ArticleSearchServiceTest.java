package com.example.springsecuritydemo;

import com.example.springsecuritydemo.models.es.ArticleEsModel;
import com.example.springsecuritydemo.service.SearchService;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

/**
 * @author ducduongn
 */
@SpringBootTest
@Slf4j
public class ArticleSearchServiceTest {
    @Autowired
    private SearchService searchService;

    @Test
    public void testSearchArticleQuery() {
        List<ArticleEsModel> articles = searchService.processSearchQuery("COVID");

        articles.forEach(article -> {
            log.info("Test results: " + article.getTitle());
        });
    }
}
