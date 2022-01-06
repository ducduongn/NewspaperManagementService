//package com.example.springsecuritydemo.config.rabbitmq;
//
//import com.example.springsecuritydemo.messaging.rabbitmq.RabbitAmqpRunner;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.boot.CommandLineRunner;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.context.annotation.Profile;
//
///**
// * @author ducduongn
// */
//@Slf4j
//@Configuration
//public class MQRunnerConfig {
//    @Profile("usage_message")
//    @Bean
//    public CommandLineRunner usage() {
//        return args -> {
//            log.info("This app uses Spring Profille to control its behavior.");
//            log.info("Sample usage: java -jar rabbit-tutorials.jar --spring.profiles.active=hello-world,sender");
//        };
//    }
//
//    @Profile("!usage_message")
//    @Bean
//    public CommandLineRunner tutorial() {
//        return new RabbitAmqpRunner();
//    }
//}
