package com.example.springsecuritydemo.config.rabbitmq;

import com.example.springsecuritydemo.messaging.rabbitmq.MQArticleWorker;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
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

    @Qualifier("crawlingMessageConverter")
    @Bean
    public MessageConverter crawlingMessageConverter() {
        return new Jackson2JsonMessageConverter();
    }

    @Qualifier("crawlingRabbitTemplate")
    @Bean
    public RabbitTemplate crawlingRabbitTemplate(
            @Qualifier("crawlingConnectionFactory")  ConnectionFactory crawlingConnectionFactory) {
        final RabbitTemplate rabbitTemplate = new RabbitTemplate(crawlingConnectionFactory);
        rabbitTemplate.setMessageConverter(crawlingMessageConverter());
        return rabbitTemplate;
    }


    @Qualifier("crawlingConnectionFactory")
    @Bean
    CachingConnectionFactory crawlingConnectionFactory() {
        CachingConnectionFactory cachingConnectionFactory = new CachingConnectionFactory(host);
        cachingConnectionFactory.setUsername(username);
        cachingConnectionFactory.setPassword(password);
        return cachingConnectionFactory;
    }

    @Qualifier("crawlingMessageListenerAdapter")
    @Bean
    public MessageListenerAdapter crawlingMessageListenerAdapter(MQArticleWorker mqArticleWorker) {
        MessageListenerAdapter messageListenerAdapter = new MessageListenerAdapter(mqArticleWorker, "crawlArticle");
        messageListenerAdapter.setMessageConverter(crawlingMessageConverter());
        return messageListenerAdapter;
    }


    @Qualifier("crawlingSimpleMessageListenerContainer")
    @Bean
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

    @Qualifier("crawlingQueue")
    @Bean
    public Queue crawlingQueue() {
        return new Queue(queueName);
    }

    @Qualifier("crawlingExchange")
    @Bean
    TopicExchange crawlingExchange() {
        return new TopicExchange(exchange);
    }

    @Qualifier("crawlingBinding")
    @Bean
    Binding crawlingBinding(Queue crawlingQueue, TopicExchange crawlingExchange) {
        return BindingBuilder.bind(crawlingQueue).to(crawlingExchange).with(routingKey);
    }
}
