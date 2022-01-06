package com.example.springsecuritydemo.config.rabbitmq;

import org.springframework.boot.autoconfigure.amqp.SimpleRabbitListenerContainerFactoryConfigurer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AbstractRabbitConfig {
    @Bean
    SimpleRabbitListenerContainerFactoryConfigurer containerFactoryConfigurer() {
        return new SimpleRabbitListenerContainerFactoryConfigurer();
    }
}
