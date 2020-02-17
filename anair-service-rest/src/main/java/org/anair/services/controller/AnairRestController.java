package org.anair.services.controller;

import lombok.extern.slf4j.Slf4j;
import org.anair.services.service.PublishService;
import org.anair.services.exception.ServiceException;
import org.anair.services.protobuf.Books;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import javax.validation.constraints.NotBlank;
import java.time.LocalDateTime;


/**
 * Controller for anair service: anair-service-rest.
 */
@RestController
@Validated
@Slf4j
public class AnairRestController {

    @Autowired
    private PublishService publishService;

    @Autowired
    @Qualifier("protoRestTemplate")
    private RestTemplate protoRestTemplate;

    @Value("${baseurl.service.proto}")
    private String baseurlServiceC;

    @GetMapping(value = "/echo/{hello}", produces = MediaType.TEXT_PLAIN_VALUE)
    public ResponseEntity<String> echo(@RequestHeader final HttpHeaders headers,
                                       @NotBlank @PathVariable("hello") final String hello) {
        log.debug("hello with headers {} body {}", headers, hello);
        return ResponseEntity.status(HttpStatus.OK).body(hello + " @ " + LocalDateTime.now());
    }

    @GetMapping(value = "/publish/{userId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> publish(@NotBlank @PathVariable("userId") final String userId) {
        try {
            publishService.publish(userId);
            ResponseEntity<Books.Book> book = protoRestTemplate.getForEntity(baseurlServiceC + "/book/1", Books.Book.class);

            if (book.getStatusCode().is2xxSuccessful()) {
                log.info("Received book for id: 1 -> {}", book.getBody().toString());
                return ResponseEntity.status(HttpStatus.OK).body(book.toString());
            } else {
                throw new ServiceException("bad response from Proto service");
            }
        }catch (Exception e){
            throw new ServiceException(ExceptionUtils.getRootCauseMessage(e));
        }
    }

}
