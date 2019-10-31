package org.anair.services.a.controller;

import org.anair.services.a.service.PublishService;
import org.anair.services.a.exception.ServiceException;
import io.swagger.annotations.*;
import org.anair.services.protobuf.Books;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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

import javax.validation.ConstraintViolationException;
import javax.validation.constraints.NotBlank;
import java.time.LocalDateTime;


/**
 * Controller for anair service: anair-service-a.
 */
@Api(value = "anair-service-a", tags = {"anair-service-a"}, consumes = MediaType.APPLICATION_JSON_VALUE)
@RestController
@Validated
public class ServiceARestController {

    private static final Logger logger = LoggerFactory.getLogger(ServiceARestController.class);

    @Autowired
    private PublishService publishService;

    @Autowired
    @Qualifier("protoRestTemplate")
    private RestTemplate protoRestTemplate;

    @Value("${baseurl.service.c}")
    private String baseurlServiceC;

    @ApiOperation(value = "Echo", notes = "For testing purpose")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successful"),
            @ApiResponse(code = 500, message = "Severe error")
    })
    @GetMapping(value = "/echo/{hello}", produces = MediaType.TEXT_PLAIN_VALUE)
    public ResponseEntity<String> echo(@RequestHeader final HttpHeaders headers, @ApiParam(value = "hello", required = true) @NotBlank @PathVariable("hello") final String hello) {
        logger.debug("hello with headers {} body {}", headers, hello);
        return ResponseEntity.status(HttpStatus.OK).body(hello + " @ " + LocalDateTime.now());
    }

    @ApiOperation(value = "Publish data", notes = "Publish data to topic anair-1")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successful"),
            @ApiResponse(code = 500, message = "Severe error")
    })
    @GetMapping(value = "/publish/{userId}")
    public ResponseEntity<String> publish(@ApiParam(value = "userId", required = true) @NotBlank @PathVariable("userId") final String userId) {
        publishService.publish(userId);
        ResponseEntity<Books.Book> book = protoRestTemplate.getForEntity(baseurlServiceC+"/book/1", Books.Book.class);

        if(book.getStatusCode().is2xxSuccessful()){
            logger.info("Received book for id: 1 -> {}", book.getBody().toString());
            return ResponseEntity.status(HttpStatus.OK).body(book.toString());
        }else{
            throw new ServiceException("bad response from service C");
        }

    }

    // ** Error handlers ** //
    @ExceptionHandler({ConstraintViolationException.class, IllegalArgumentException.class})
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    ResponseEntity<String> handleInvalidRequestException(Exception e) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid request: " + ExceptionUtils.getRootCauseMessage(e));
    }

    @ExceptionHandler(ServiceException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    ResponseEntity<String> handleSevereException(RuntimeException e) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ExceptionUtils.getRootCauseMessage(e));
    }

}
