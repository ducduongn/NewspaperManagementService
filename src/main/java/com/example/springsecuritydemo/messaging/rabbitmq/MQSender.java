package com.example.springsecuritydemo.messaging.rabbitmq;

import com.example.springsecuritydemo.models.articles.Article;
import com.example.springsecuritydemo.models.dto.ArticleDto;
import com.google.gson.Gson;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

/**
 * @author ducduongn
 */
@Slf4j
@Component
public class MQSender {
    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Autowired
    private Queue queue;

    public void send(ArticleDto articleDto) {
//        String jsonArticle = new Gson().toJson(article);

        log.info("Article is being sent!");
        rabbitTemplate.convertAndSend(queue.getName(), articleDto);
        log.info("Article has been sent!");
    }
}
