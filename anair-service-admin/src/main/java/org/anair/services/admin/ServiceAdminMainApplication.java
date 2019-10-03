package org.anair.services.admin;

import de.codecentric.boot.admin.server.config.EnableAdminServer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@EnableAdminServer
@SpringBootApplication
@ComponentScan(basePackages = "org.anair.services")
public class ServiceAdminMainApplication implements WebMvcConfigurer {

    private static final Logger logger = LoggerFactory.getLogger(ServiceAdminMainApplication.class);

    public static void main(String[] args) {
        ApplicationContext context = SpringApplication.run(ServiceAdminMainApplication.class, args);
        logger.info("Application Context: {}", context.getApplicationName());
    }

}

