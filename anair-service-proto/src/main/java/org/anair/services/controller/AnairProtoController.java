package org.anair.services.controller;

import lombok.extern.slf4j.Slf4j;
import org.anair.services.exception.ServiceException;
import org.anair.services.protobuf.Books;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.ConstraintViolationException;
import javax.validation.constraints.NotBlank;


/**
 * Controller for anair service: anair-service-proto.
 */
@RestController
@Validated
@Slf4j
public class AnairProtoController {

    @GetMapping(value = "/book/{id}", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Books.Book> getBookById(@NotBlank @PathVariable("id") final String id) {
        Books.Book book = Books.Book.newBuilder()
                .setId(1)
                .setName("Book1")
                .addAuthor(Books.Author.newBuilder()
                                .setId(0)
                                .setFirstName("fname1")
                                .setLastName("lname1"))
                .build();
        log.info("Return book data in proto type");
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
