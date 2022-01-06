package com.example.springsecuritydemo.messaging.rabbitmq;

import com.example.springsecuritydemo.models.articles.Article;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * @author ducduongn
 */
@Slf4j
@Component
public class MQSynchronizeSender {
    @Value("${spring.rabbitmq.sync-queue}")
    private String queueName;

    @Value("${spring.rabbitmq.sync-exchange}")
    private String exchange;

    @Value("${spring.rabbitmq.sync-routingkey}")
    private String routingKey;

    @Qualifier("synchronizeRabbitTemplate")
    @Autowired
    private RabbitTemplate synchronizeRabbitTemplate;

    @Qualifier("synchronizeQueue")
    @Autowired
    private Queue queue;

    public void send(Article article) {
        log.info("Article is being sent!");
        synchronizeRabbitTemplate.convertAndSend(exchange, routingKey, article);
        log.info("Article has been sent!");
    }
}
