package com.example.springsecuritydemo.config.rabbitmq;

import com.example.springsecuritydemo.messaging.rabbitmq.MQArticleWorker;
import com.example.springsecuritydemo.messaging.rabbitmq.MQSynchronizeWorker;
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
public class SynchronizeMQConfig {
    @Value("${spring.rabbitmq.host}")
    private String host;

    @Value("${spring.rabbitmq.username}")
    private String username;

    @Value("${spring.rabbitmq.password}")
    private String password;

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

    @Bean(name = "synchronizeSimpleMessageListenerContainer")
    public SimpleMessageListenerContainer synchronizeSimpleMessageListenerContainer(
            @Qualifier("synchronizeConnectionFactory") ConnectionFactory synchronizeConnectionFactory,
            @Qualifier("synchronizeListenerAdapter") MessageListenerAdapter synchronizeListenerAdapter) {
        SimpleMessageListenerContainer synchronizeContainer = new SimpleMessageListenerContainer();
        synchronizeContainer.setConnectionFactory(synchronizeConnectionFactory);
        synchronizeContainer.setQueueNames(queueName);
        synchronizeContainer.setConcurrentConsumers(5);
        synchronizeContainer.setMaxConcurrentConsumers(6);
        synchronizeContainer.setMessageListener(synchronizeListenerAdapter);
        return synchronizeContainer;
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

    @Bean(name = "synchronizeAdmin")
    public AmqpAdmin pimAmqpAdmin(@Qualifier("synchronizeConnectionFactory") ConnectionFactory connectionFactory) {
        return new RabbitAdmin(connectionFactory);
    }
}
