package com.example.springsecuritydemo.config.rabbitmq;

import com.example.springsecuritydemo.messaging.rabbitmq.MQArticleWorker;
import com.example.springsecuritydemo.messaging.rabbitmq.MQSynchronizeWorker;
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

    @Qualifier("synchronizeMessageConverter")
    @Bean
    public MessageConverter synchronizeMessageConverter() {
        return new Jackson2JsonMessageConverter();
    }

    @Qualifier("synchronizeRabbitTemplate")
    @Bean
    public RabbitTemplate synchronizeRabbitTemplate(
            @Qualifier("synchronizeConnectionFactory")ConnectionFactory synchronizeConnectionFactory)
    {
        final RabbitTemplate rabbitTemplate = new RabbitTemplate(synchronizeConnectionFactory);
        rabbitTemplate.setMessageConverter(synchronizeMessageConverter());
        return rabbitTemplate;
    }


    @Bean
    @Qualifier("synchronizeConnectionFactory")
    CachingConnectionFactory synchronizeConnectionFactory() {
        CachingConnectionFactory cachingConnectionFactory = new CachingConnectionFactory(host);
        cachingConnectionFactory.setUsername(username);
        cachingConnectionFactory.setPassword(password);
        return cachingConnectionFactory;
    }

    @Bean
    @Qualifier("synchronizeListenerAdapter")
    public MessageListenerAdapter synchronizeListenerAdapter(MQSynchronizeWorker mqArticleWorker) {
        MessageListenerAdapter messageListenerAdapter =
                new MessageListenerAdapter(mqArticleWorker,
                "synchronizeArticleFromJpaToEs");
        messageListenerAdapter.setMessageConverter(synchronizeMessageConverter());
        return messageListenerAdapter;
    }

    @Qualifier("synchronizeSimpleMessageListenerContainer")
    @Bean
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

    @Qualifier("synchronizeQueue")
    @Bean
    public Queue synchronizeQueue() {
        return new Queue(queueName);
    }

    @Qualifier("synchronizeExchange")
    @Bean
    TopicExchange synchronizeExchange() {
        return new TopicExchange(exchange);
    }

    @Qualifier("synchronizeBinding")
    @Bean
    Binding synchronizeBinding(Queue synchronizeQueue, TopicExchange synchronizeExchange) {
        return BindingBuilder.bind(synchronizeQueue).to(synchronizeExchange).with(routingKey);
    }
}
