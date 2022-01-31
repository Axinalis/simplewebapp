package com.mastery.java.task;

import org.apache.activemq.ActiveMQConnectionFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import javax.jms.ConnectionFactory;

@EnableJpaRepositories("com.mastery.java.task.repository")
@EnableSwagger2
@SpringBootApplication
public class MainApp {

    @Value("${active-mq.broker-url}")
    private String brokerUrl;
    @Value("${active-mq.user}")
    private String user;
    @Value("${active-mq.password}")
    private String password;

    public static void main(String[] args) {
        SpringApplication.run(MainApp.class, args);
    }

    @Bean
    public ConnectionFactory connectionFactory(){
        ActiveMQConnectionFactory factory  = new ActiveMQConnectionFactory();
        factory.setBrokerURL(brokerUrl);
        factory.setUserName(user);
        factory.setPassword(password);
        factory.setTrustAllPackages(true);
        return factory;
    }

}


