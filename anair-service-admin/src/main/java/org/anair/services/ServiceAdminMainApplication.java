package org.anair.services;

import de.codecentric.boot.admin.server.config.EnableAdminServer;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@EnableAdminServer
@SpringBootApplication
@Slf4j
public class ServiceAdminMainApplication implements WebMvcConfigurer {

    public static void main(String[] args) {
        ApplicationContext context = SpringApplication.run(ServiceAdminMainApplication.class, args);
        log.info("Application Context: {}", context.getApplicationName());
    }

}

