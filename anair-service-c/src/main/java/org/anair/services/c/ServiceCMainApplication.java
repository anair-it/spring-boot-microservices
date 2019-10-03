package org.anair.services.c;

import org.anair.services.c.interceptor.MDCInterceptor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.http.converter.protobuf.ProtobufHttpMessageConverter;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@SpringBootApplication
@ComponentScan(basePackages = "org.anair.services")
public class ServiceCMainApplication implements WebMvcConfigurer {

    private static final Logger logger = LoggerFactory.getLogger(ServiceCMainApplication.class);

    public static void main(String[] args) {
        ApplicationContext context = SpringApplication.run(ServiceCMainApplication.class, args);
        logger.info("Application Context: {}", context.getApplicationName());
    }

    @Bean
    public ProtobufHttpMessageConverter protobufHttpMessageConverter() {
        return new ProtobufHttpMessageConverter();
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new MDCInterceptor());
    }
}

