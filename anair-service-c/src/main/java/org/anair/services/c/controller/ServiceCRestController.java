package org.anair.services.c.controller;

import org.anair.services.c.exception.ServiceException;
import io.swagger.annotations.*;
import org.anair.services.protobuf.Books;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.ConstraintViolationException;
import javax.validation.constraints.NotBlank;
import java.time.LocalDateTime;


/**
 * Controller for anair service: anair-service-c.
 */
@Api(value = "anair-service-c", tags = {"anair-service-c"}, consumes = MediaType.APPLICATION_JSON_VALUE)
@RestController
@Validated
public class ServiceCRestController {

    private static final Logger logger = LoggerFactory.getLogger(ServiceCRestController.class);

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

    @ApiOperation(value = "gRPC endpoint", notes = "Return data as protobuf")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successful"),
            @ApiResponse(code = 500, message = "Severe error")
    })
    @GetMapping(value = "/book/{id}")
    public ResponseEntity<Books.Book> getBookById(@ApiParam(value = "input", required = true) @NotBlank @PathVariable("id") final String id) {
        Books.Book book = Books.Book.newBuilder()
                .setId(1)
                .setName("Book1")
                .addAuthor(Books.Author.newBuilder()
                                .setId(0)
                                .setFirstName("fname1")
                                .setLastName("lname1"))
                .build();
        logger.info("Return book data in proto type");
        return ResponseEntity.status(HttpStatus.OK).body(book);
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
