package org.anair.fn.controller;

import lombok.extern.slf4j.Slf4j;
import org.anair.fn.exception.ServiceException;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageConversionException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

import javax.validation.ValidationException;

@ControllerAdvice
@ResponseBody
@RequestMapping(produces = MediaType.APPLICATION_JSON_VALUE)
@Slf4j
public class RestControllerErrorHandler {

    @ExceptionHandler(ServiceException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    ResponseEntity<String> handleSevereException(RuntimeException e) {
        ResponseEntity<String> responseEntity = ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ExceptionUtils.getRootCauseMessage(e));
        log.error(responseEntity.toString());
        throw e;
    }

    @ExceptionHandler({HttpMessageConversionException.class, MethodArgumentNotValidException.class, ValidationException.class, IllegalArgumentException.class})
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    ResponseEntity<String> handleInvalidRequestException(Exception e) {
        ResponseEntity<String> responseEntity = ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid request: " + ExceptionUtils.getRootCauseMessage(e));
        log.error(responseEntity.toString());
        return responseEntity;
    }

}
