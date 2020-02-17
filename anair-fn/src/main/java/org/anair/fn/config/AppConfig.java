package org.anair.fn.config;

import org.anair.fn.controller.MyRestController;
import org.anair.fn.service.MySqsListener;
import org.anair.fn.service.MySqsPublisher;
import org.anair.fn.service.S3Service;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Configuration
@Import({S3Service.class, MySqsListener.class, MySqsPublisher.class, MyRestController.class})
public class AppConfig {
}
