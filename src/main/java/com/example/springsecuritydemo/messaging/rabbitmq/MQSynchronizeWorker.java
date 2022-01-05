package com.example.springsecuritydemo.messaging.rabbitmq;

import com.example.springsecuritydemo.es.repository.EsArticleRepository;
import com.example.springsecuritydemo.models.articles.Article;
import com.example.springsecuritydemo.repository.ArticleRepository;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author ducduongn
 */
@Service
@Slf4j
public class MQSynchronizeWorker {
    @Autowired
    private ArticleRepository articleRepository;

    @Autowired
    private EsArticleRepository esArticleRepository;

    @Autowired
    private ModelMapper modelMapper;

    public void synchronizeArticleFromJpaToEs(Article article) {
        log.info("SynchronizeWorker has receive: " + article.getTitle());
    }
}
