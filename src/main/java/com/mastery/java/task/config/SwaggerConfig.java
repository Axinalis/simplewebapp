package com.mastery.java.task.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger.web.InMemorySwaggerResourcesProvider;
import springfox.documentation.swagger.web.SwaggerResource;
import springfox.documentation.swagger.web.SwaggerResourcesProvider;

import java.util.ArrayList;
import java.util.List;

import static springfox.documentation.spi.DocumentationType.SWAGGER_2;

@Configuration
public class SwaggerConfig {

    @Value("${app.version}")
    private String version;
    @Value("${app.description}")
    private String description;
    @Value("${app.title}")
    private String title;

    //For default documentation
    @Bean
    public Docket swagger() {
        return new Docket(SWAGGER_2)
                .select()
                .apis(RequestHandlerSelectors.basePackage("com.mastery.java.task"))
                .build()
                .apiInfo(apiInformation());
    }

    //For custom documentation from yaml file
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
