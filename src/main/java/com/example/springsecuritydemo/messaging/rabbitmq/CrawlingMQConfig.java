package com.example.springsecuritydemo.messaging.rabbitmq;

import com.example.springsecuritydemo.messaging.rabbitmq.MQArticleWorker;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.annotation.EnableRabbit;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.rabbit.listener.SimpleMessageListenerContainer;
import org.springframework.amqp.rabbit.listener.adapter.MessageListenerAdapter;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author ducduongn
 */
@EnableRabbit
@Configuration
public class CrawlingMQConfig {
    @Value("${spring.rabbitmq.host}")
    private String host;

    @Value("${spring.rabbitmq.username}")
    private String username;

    @Value("${spring.rabbitmq.password}")
    private String password;

    @Value("${spring.rabbitmq.queue}")
    private String queueName;

    @Value("${spring.rabbitmq.exchange}")
    private String exchange;

    @Value("${spring.rabbitmq.routingkey}")
    private String routingKey;

    @Bean(name = "crawlingMessageConverter")
    public MessageConverter crawlingMessageConverter() {
        ObjectMapper mapper = new ObjectMapper().findAndRegisterModules();

        return new Jackson2JsonMessageConverter(mapper);
    }

    @Bean(name = "crawlingRabbitTemplate")
    public RabbitTemplate crawlingRabbitTemplate(
            @Qualifier("crawlingConnectionFactory")  ConnectionFactory crawlingConnectionFactory) {
        final RabbitTemplate rabbitTemplate = new RabbitTemplate(crawlingConnectionFactory);
        rabbitTemplate.setMessageConverter(crawlingMessageConverter());
        return rabbitTemplate;
    }


    @Bean(name = "crawlingConnectionFactory")
    CachingConnectionFactory crawlingConnectionFactory() {
        CachingConnectionFactory cachingConnectionFactory = new CachingConnectionFactory(host);
        cachingConnectionFactory.setUsername(username);
        cachingConnectionFactory.setPassword(password);
        return cachingConnectionFactory;
    }

    @Bean(name = "crawlingMessageListenerAdapter")
    public MessageListenerAdapter crawlingMessageListenerAdapter(MQArticleWorker mqArticleWorker) {
        MessageListenerAdapter messageListenerAdapter = new MessageListenerAdapter(mqArticleWorker, "crawlArticle");
        messageListenerAdapter.setMessageConverter(crawlingMessageConverter());
        return messageListenerAdapter;
    }


    @Bean(name = "crawlingSimpleMessageListenerContainer")
    public SimpleMessageListenerContainer crawlingSimpleMessageListenerContainer(
            @Qualifier("crawlingConnectionFactory") ConnectionFactory crawlingConnectionFactory,
            @Qualifier("crawlingMessageListenerAdapter") MessageListenerAdapter crawlingMessageListenerAdapter) {

        SimpleMessageListenerContainer crawlingContainer = new SimpleMessageListenerContainer();
        crawlingContainer.setConnectionFactory(crawlingConnectionFactory);
        crawlingContainer.setQueueNames(queueName);
        crawlingContainer.setConcurrentConsumers(5);
        crawlingContainer.setMaxConcurrentConsumers(6);
        crawlingContainer.setMessageListener(crawlingMessageListenerAdapter);

        return crawlingContainer;
    }

    @Bean(name = "crawlingQueue")
    public Queue crawlingQueue() {
        return new Queue(queueName);
    }

    @Bean(name = "crawlingExchange")
    TopicExchange crawlingExchange() {
        return new TopicExchange(exchange);
    }


    @Bean(name = "crawlingBinding")
    Binding crawlingBinding(
            @Qualifier("crawlingQueue") Queue crawlingQueue,
            @Qualifier("crawlingExchange") TopicExchange crawlingExchange) {
        return BindingBuilder.bind(crawlingQueue).to(crawlingExchange).with(routingKey);
    }

    @Bean(name = "crawlerAdmin")
    public AmqpAdmin amqpAdmin(@Qualifier("crawlingConnectionFactory") ConnectionFactory connectionFactory) {
        return new RabbitAdmin(connectionFactory);
    }
}
