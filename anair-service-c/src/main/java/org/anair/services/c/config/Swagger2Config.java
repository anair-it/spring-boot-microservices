package org.anair.services.c.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.util.Arrays;
import java.util.HashSet;


@Configuration
@EnableSwagger2
public class Swagger2Config {

    @Bean
    public Docket api() {
        return new Docket(DocumentationType.SWAGGER_2)
                .groupName("anair")
                .protocols(new HashSet<>(Arrays.asList("http", "https")))
                .select()
                .apis(RequestHandlerSelectors
                        .basePackage("org.anair.services.c"))
                .paths(PathSelectors.any())
                .build()
                .apiInfo(apiEndPointsInfo());
    }

    private ApiInfo apiEndPointsInfo() {
        return new ApiInfoBuilder().title("ANAIR - anair-service-c Rest API")
                .description("ANAIR - anair-service-c service endpoints")
                .contact(new Contact("Anoop Nair", "anoopnair.it@gmail.com", null))
                .version("0.0.1")
                .build();
    }
}
