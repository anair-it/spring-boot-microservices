package org.anair.fn;

import lombok.extern.slf4j.Slf4j;
import org.anair.fn.config.AppConfig;
import org.anair.fn.config.AwsConfig;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Import;

@SpringBootApplication
@ComponentScan(lazyInit = true, useDefaultFilters = false)
@Import({AwsConfig.class, AppConfig.class})
@Slf4j
public class MyApplication {

    public static void main(String[] args) {
        ApplicationContext context = SpringApplication.run(MyApplication.class, args);
        log.info("Application Context: {}", context.getApplicationName());
    }

}

