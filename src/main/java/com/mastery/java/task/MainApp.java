package com.mastery.java.task;

import com.mastery.java.task.config.AppConfiguration;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class MainApp {

    public static void main(String[] args) {
        SpringApplication.run(AppConfiguration.class, args);
    }

}
