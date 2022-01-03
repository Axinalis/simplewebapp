package com.mastery.java.task.config;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@Configuration
@ComponentScan("com.mastery.java.task")
@EnableJpaRepositories("com.mastery.java.task.jpa")
@PropertySource("classpath:application.properties")
public class AppConfiguration {

}
