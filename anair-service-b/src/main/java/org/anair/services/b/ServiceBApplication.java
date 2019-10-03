package org.anair.services.b;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(basePackages = "org.anair.services")
public class ServiceBApplication {

    private static final Logger logger = LoggerFactory.getLogger(ServiceBApplication.class);

    public static void main(String[] args) {
        ApplicationContext context = SpringApplication.run(ServiceBApplication.class, args);
        logger.info("Application Context: {}", context.getApplicationName());
    }

}

