package com.example.springsecuritydemo.messaging.rabbitmq;

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
public class MQCrawlingSender {
    @Value("${spring.rabbitmq.queue}")
    private String queueName;

    @Value("${spring.rabbitmq.exchange}")
    private String exchange;

    @Value("${spring.rabbitmq.routingkey}")
    private String routingKey;

    @Qualifier("crawlingRabbitTemplate")
    @Autowired
    private RabbitTemplate crawlingRabbitTemplate;

    @Qualifier("crawlingQueue")
    @Autowired
    private Queue queue;

    public void send(String articleUrl) {
        log.info("Article is being sent!");
        crawlingRabbitTemplate.convertAndSend(exchange, routingKey, articleUrl);
        log.info("Article has been sent!");
    }
}
