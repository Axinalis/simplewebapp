package com.mastery.java.task.config;

import com.mastery.java.task.dao.EmployeeDao;
import com.mastery.java.task.dao.impl.DefaultEmployeeDao;
import com.mastery.java.task.dto.Employee;
import com.mastery.java.task.rest.EmployeeController;
import com.mastery.java.task.service.EmployeeService;
import com.mastery.java.task.service.impl.DefaultEmployeeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.*;
import org.springframework.jdbc.core.JdbcTemplate;

@SpringBootApplication
@ComponentScan("com.mastery.java.task")
@PropertySource("classpath:application.properties")
public class AppConfiguration {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    public static void main(String[] args) {
        SpringApplication.run(AppConfiguration.class, args);
    }

    @Bean
    public EmployeeDao employeeDao(){
        return new DefaultEmployeeDao(jdbcTemplate);
    }

    @Bean
    public EmployeeService employeeService(){
        return new DefaultEmployeeService(employeeDao());
    }

}
