package com.example.springsecuritydemo.es.repository;

import com.example.springsecuritydemo.models.es.ArticleEsModel;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;


@SpringBootTest
@Slf4j
public class EsSearchRepoTest {
    @Autowired
    private EsArticleRepository esArticleRepository;

    @Test
    public void testEsArticleSearch() {
        ArticleEsModel articleEsModel = esArticleRepository.findByUrl("https://vnexpress.net/sri-lanka-co-the-vo-no-nam-nay-4411531.html");

        log.info(articleEsModel.getTitle());
    }
}
