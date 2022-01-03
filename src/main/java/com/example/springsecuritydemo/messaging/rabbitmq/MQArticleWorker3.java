package com.example.springsecuritydemo.messaging.rabbitmq;

import com.example.springsecuritydemo.models.articles.Article;
import com.example.springsecuritydemo.models.dto.ArticleDto;
import com.example.springsecuritydemo.repository.ArticleRepository;
import com.example.springsecuritydemo.utils.crawler.DateTimeConverter;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author ducduongn
 */
@Service
@Slf4j
@RabbitListener(queues = "article_queue")
public class MQArticleWorker3 {
    @Autowired
    private ArticleRepository articleRepository;

    @Autowired
    private ModelMapper modelMapper;

    @RabbitHandler
    public void receiver(ArticleDto articleDto) {
        log.info("Receiver has receive a message: " + articleDto.getTitle());

        Article article = modelMapper.map(articleDto, Article.class);

        article.setPostedDate(DateTimeConverter
                .convertDateTimeStringToLocalDateTime(article.getStringPostedDate()));

        if (!articleRepository.existsByUrl(article.getUrl())) {
            articleRepository.save(article);
            log.info("Save article sucessfully: " + article.getTitle());
        }
    }
}
