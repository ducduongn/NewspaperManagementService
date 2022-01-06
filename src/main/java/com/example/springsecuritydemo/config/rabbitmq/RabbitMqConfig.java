package com.example.springsecuritydemo.config.rabbitmq;

import com.example.springsecuritydemo.messaging.rabbitmq.MQArticleWorker;
import com.example.springsecuritydemo.messaging.rabbitmq.MQSender;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.rabbit.config.SimpleRabbitListenerContainerFactory;
import org.springframework.amqp.rabbit.config.SimpleRabbitListenerEndpoint;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.rabbit.listener.SimpleMessageListenerContainer;
import org.springframework.amqp.rabbit.listener.adapter.MessageListenerAdapter;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.amqp.SimpleRabbitListenerContainerFactoryConfigurer;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


/**
 * @author ducduongn
 */

@Configuration
public class CrawlingMQConfig {
    @Value("${spring.rabbitmq.host}")
    private String host;

    @Value("${spring.rabbitmq.port}")
    private int port;

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

<<<<<<< Updated upstream:src/main/java/com/example/springsecuritydemo/config/rabbitmq/RabbitMqConfig.java
    @Bean
    CachingConnectionFactory connectionFactory() {
        CachingConnectionFactory cachingConnectionFactory = new CachingConnectionFactory(host);
        cachingConnectionFactory.setUsername(username);
        cachingConnectionFactory.setPassword(password);
        return cachingConnectionFactory;
    }

    @Bean
    public MessageConverter jsonMessageConverter() {
        return new Jackson2JsonMessageConverter();
    }

    @Bean
    public RabbitTemplate rabbitTemplate(ConnectionFactory connectionFactory) {
        final RabbitTemplate rabbitTemplate = new RabbitTemplate(connectionFactory);
        rabbitTemplate.setMessageConverter(jsonMessageConverter());
        return rabbitTemplate;
    }

    @Bean
    public MessageListenerAdapter listenerAdapter(MQArticleWorker mqArticleWorker) {
=======
    @Bean(name = "crawlingMessageConverter")
    public MessageConverter crawlingMessageConverter() {
        return new Jackson2JsonMessageConverter();
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
        cachingConnectionFactory.setPort(port);
        cachingConnectionFactory.setUsername(username);
        cachingConnectionFactory.setPassword(password);
        return cachingConnectionFactory;
    }

    @Bean(name = "crawlingMessageListenerAdapter")
    public MessageListenerAdapter crawlingMessageListenerAdapter(MQArticleWorker mqArticleWorker) {
>>>>>>> Stashed changes:src/main/java/com/example/springsecuritydemo/config/rabbitmq/CrawlingMQConfig.java
        MessageListenerAdapter messageListenerAdapter = new MessageListenerAdapter(mqArticleWorker, "crawlArticle");
        messageListenerAdapter.setMessageConverter(jsonMessageConverter());
        return messageListenerAdapter;
    }

<<<<<<< Updated upstream:src/main/java/com/example/springsecuritydemo/config/rabbitmq/RabbitMqConfig.java
    @Bean
    public SimpleMessageListenerContainer simpleMessageListenerContainer(ConnectionFactory connectionFactory,
                                                          MessageListenerAdapter listenerAdapter) {
        SimpleMessageListenerContainer container = new SimpleMessageListenerContainer();
        container.setConnectionFactory(connectionFactory);
        container.setQueueNames(queueName);
        container.setConcurrentConsumers(5);
        container.setMaxConcurrentConsumers(6);
        container.setMessageListener(listenerAdapter);
        return container;
    }

    @Bean
    public Queue queue() {
        return new Queue("article_queue");
    }

    @Bean
    TopicExchange exchange() {
        return new TopicExchange(exchange);
    }


    @Bean(name = "crawlingSimpleRabbitListenerContainerFactory")
    public SimpleRabbitListenerContainerFactory simpleRabbitListenerContainerFactory (
            SimpleRabbitListenerContainerFactoryConfigurer containerFactoryConfigurer,
            @Qualifier("crawlingConnectionFactory") ConnectionFactory crawlingConnectionFactory
    ) {
        SimpleRabbitListenerContainerFactory simpleRabbitListenerContainerFactory = new SimpleRabbitListenerContainerFactory();

        containerFactoryConfigurer.configure(simpleRabbitListenerContainerFactory, crawlingConnectionFactory);
        simpleRabbitListenerContainerFactory.setConcurrentConsumers(5);
        simpleRabbitListenerContainerFactory.setMaxConcurrentConsumers(6);

        return simpleRabbitListenerContainerFactory;
    }

    @Bean(name = "crawlingSimpleMessageListenerContainer")
    public SimpleMessageListenerContainer crawlingSimpleMessageListenerContainer(
            @Qualifier("crawlingSimpleRabbitListenerContainerFactory") SimpleRabbitListenerContainerFactory simpleRabbitListenerContainerFactory,
            @Qualifier("crawlingMessageListenerAdapter") MessageListenerAdapter crawlingMessageListenerAdapter) {

        SimpleRabbitListenerEndpoint endpoint = new SimpleRabbitListenerEndpoint();

        endpoint.setQueueNames(queueName);
        endpoint.setMessageListener(crawlingMessageListenerAdapter);

        return simpleRabbitListenerContainerFactory.createListenerContainer(endpoint);
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
>>>>>>> Stashed changes:src/main/java/com/example/springsecuritydemo/config/rabbitmq/CrawlingMQConfig.java
    }

}
