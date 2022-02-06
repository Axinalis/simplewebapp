package com.mastery.java.task.config;

import org.apache.activemq.ActiveMQConnectionFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jms.annotation.EnableJms;

import javax.jms.ConnectionFactory;

@EnableJms
@Configuration
public class ActiveMQConfig {

    @Value("${active-mq.broker-url}")
    private String brokerUrl;
    @Value("${active-mq.user}")
    private String user;
    @Value("${active-mq.password}")
    private String password;

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
