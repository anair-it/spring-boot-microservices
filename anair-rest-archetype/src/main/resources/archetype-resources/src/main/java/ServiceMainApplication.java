#set( $symbol_pound = '#' )
#set( $symbol_dollar = '$' )
#set( $symbol_escape = '\' )
package ${package};

import ${package}.interceptor.MDCInterceptor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@SpringBootApplication
@ComponentScan(basePackages = "${groupId}")
public class ServiceMainApplication implements WebMvcConfigurer {

    private static final Logger logger = LoggerFactory.getLogger(ServiceMainApplication.class);

    public static void main(String[] args) {
        ApplicationContext context = SpringApplication.run(ServiceMainApplication.class, args);
        logger.info("Application Context: {}", context.getApplicationName());
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new MDCInterceptor());
    }
}

