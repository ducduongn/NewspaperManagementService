package com.example.springsecuritydemo.config.rabbitmq;

import com.example.springsecuritydemo.messaging.rabbitmq.MQArticleWorker;
import com.example.springsecuritydemo.messaging.rabbitmq.MQSynchronizeWorker;
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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.amqp.SimpleRabbitListenerContainerFactoryConfigurer;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author ducduongn
 */
@Configuration
@ConfigurationProperties("spring.rabbitmq.second")
public class SynchronizeMQConfig {
    @Value("${spring.rabbitmq.host}")
    private String host;

    @Value("${spring.rabbitmq.username}")
    private String username;

    @Value("${spring.rabbitmq.password}")
    private String password;

    @Value("${spring.rabbitmq.port}")
    private int port;

    @Value("${spring.rabbitmq.sync-queue}")
    private String queueName;

    @Value("${spring.rabbitmq.sync-exchange}")
    private String exchange;

    @Value("${spring.rabbitmq.sync-routingkey}")
    private String routingKey;

    @Bean(name = "synchronizeMessageConverter")
    public MessageConverter synchronizeMessageConverter() {
        return new Jackson2JsonMessageConverter();
    }

    @Bean(name = "synchronizeRabbitTemplate")
    public RabbitTemplate synchronizeRabbitTemplate(
            @Qualifier("synchronizeConnectionFactory")ConnectionFactory synchronizeConnectionFactory)
    {
        final RabbitTemplate rabbitTemplate = new RabbitTemplate(synchronizeConnectionFactory);
        rabbitTemplate.setMessageConverter(synchronizeMessageConverter());
        return rabbitTemplate;
    }

    @Bean(name = "synchronizeConnectionFactory")
    CachingConnectionFactory synchronizeConnectionFactory() {
        CachingConnectionFactory cachingConnectionFactory = new CachingConnectionFactory(host);
        cachingConnectionFactory.setPort(port);
        cachingConnectionFactory.setUsername(username);
        cachingConnectionFactory.setPassword(password);
        return cachingConnectionFactory;
    }

    @Bean(name = "synchronizeListenerAdapter")
    public MessageListenerAdapter synchronizeListenerAdapter(MQSynchronizeWorker mqArticleWorker) {
        MessageListenerAdapter messageListenerAdapter =
                new MessageListenerAdapter(mqArticleWorker,
                "synchronizeArticleFromJpaToEs");
        messageListenerAdapter.setMessageConverter(synchronizeMessageConverter());
        return messageListenerAdapter;
    }

    @Bean(name = "synchronizeSimpleRabbitListenerContainerFactory")
    public SimpleRabbitListenerContainerFactory simpleRabbitListenerContainerFactory (
            SimpleRabbitListenerContainerFactoryConfigurer containerFactoryConfigurer,
            @Qualifier("synchronizeConnectionFactory") ConnectionFactory synchronizeConnectionFactory
    ) {
        SimpleRabbitListenerContainerFactory simpleRabbitListenerContainerFactory = new SimpleRabbitListenerContainerFactory();

        containerFactoryConfigurer.configure(simpleRabbitListenerContainerFactory, synchronizeConnectionFactory);
        simpleRabbitListenerContainerFactory.setConcurrentConsumers(5);
        simpleRabbitListenerContainerFactory.setMaxConcurrentConsumers(6);

        return simpleRabbitListenerContainerFactory;
    }

    @Bean(name = "synchronizeSimpleMessageListenerContainer")
    public SimpleMessageListenerContainer synchronizeSimpleMessageListenerContainer(
            @Qualifier("synchronizeSimpleRabbitListenerContainerFactory") SimpleRabbitListenerContainerFactory simpleRabbitListenerContainerFactory,
            @Qualifier("synchronizeListenerAdapter") MessageListenerAdapter synchronizeMessageListenerAdapter) {

        SimpleRabbitListenerEndpoint endpoint = new SimpleRabbitListenerEndpoint();

        endpoint.setQueueNames(queueName);
        endpoint.setMessageListener(synchronizeMessageListenerAdapter);

        return simpleRabbitListenerContainerFactory.createListenerContainer(endpoint);
    }

    @Bean(name = "synchronizeQueue")
    public Queue synchronizeQueue() {
        return new Queue(queueName);
    }

    @Bean(name = "synchronizeExchange")
    TopicExchange synchronizeExchange() {
        return new TopicExchange(exchange);
    }

    @Bean(name = "synchronizeBinding")
    Binding synchronizeBinding(
            @Qualifier("synchronizeQueue") Queue synchronizeQueue,
            @Qualifier("synchronizeExchange") TopicExchange synchronizeExchange) {
        return BindingBuilder.bind(synchronizeQueue).to(synchronizeExchange).with(routingKey);
    }
}
