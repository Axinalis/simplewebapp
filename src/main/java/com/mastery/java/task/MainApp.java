package com.mastery.java.task;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger.web.InMemorySwaggerResourcesProvider;
import springfox.documentation.swagger.web.SwaggerResource;
import springfox.documentation.swagger.web.SwaggerResourcesProvider;
import springfox.documentation.swagger2.annotations.EnableSwagger2;
import springfox.documentation.swagger2.annotations.EnableSwagger2WebMvc;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static springfox.documentation.spi.DocumentationType.SWAGGER_2;

@EnableJpaRepositories("com.mastery.java.task.repository")
@EnableSwagger2
@SpringBootApplication
public class MainApp {

    @Value("${app.version}")
    private String version;
    @Value("${app.description}")
    private String description;
    @Value("${app.title}")
    private String title;

    public static void main(String[] args) {
        SpringApplication.run(MainApp.class, args);
    }

    //For default documentation
    @Bean
    public Docket swagger() {
        return new Docket(SWAGGER_2)
                .select()
                .apis(RequestHandlerSelectors.basePackage("com.mastery.java.task"))
                .build()
                .apiInfo(apiInformation());
    }

    //For "Documentation" from yaml file
    @Primary
    @Bean
    public SwaggerResourcesProvider swaggerResourcesProvider(InMemorySwaggerResourcesProvider defaultResourcesProvider) {
        return () -> {
            SwaggerResource wsResource = new SwaggerResource();
            wsResource.setName("Documentation");
            wsResource.setSwaggerVersion("2.0");
            wsResource.setLocation("/app-api.yaml");

            List<SwaggerResource> resources = new ArrayList<>(defaultResourcesProvider.get());
            resources.add(wsResource);
            return resources;
        };
    }

    private ApiInfo apiInformation(){
        ApiInfoBuilder builder = new ApiInfoBuilder();
        builder.description(description);
        builder.title(title);
        builder.version(version);
        builder.termsOfServiceUrl("Free to use");
        builder.contact(new Contact("Axinalis", "https://github.io/Axinalis", "antontrus@gmail.com"));
        builder.license("API License");
        builder.licenseUrl("some site");

        return builder.build();
    }
}


