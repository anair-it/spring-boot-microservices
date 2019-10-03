#set( $symbol_pound = '#' )
#set( $symbol_dollar = '$' )
#set( $symbol_escape = '\' )
package ${package}.controller;

import ${package}.service.SampleService;
import ${package}.model.SampleModel;
import ${package}.exception.ServiceException;
import io.swagger.annotations.*;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
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
 * Controller for anair service: ${artifactId}.
 */
@Api(value = "${artifactId}", tags = {"${artifactId}"}, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
@RestController
@Validated
public class SampleRestController {

    private static final Logger logger = LoggerFactory.getLogger(SampleRestController.class);

    @Autowired
    private SampleService sampleService;

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

    @ApiOperation(value = "Sample service endpoint", notes = "Add endpoint description here")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successful"),
            @ApiResponse(code = 500, message = "Severe error")
    })
    @GetMapping(value = "/sample/{input}", produces = MediaType.TEXT_PLAIN_VALUE)
    public ResponseEntity<String> sample(@ApiParam(value = "input", required = true) @NotBlank @PathVariable("input") final String input) {
        SampleModel sampleModel = new SampleModel();

        //Make service call
        sampleModel.setInput(sampleService.sampleMethod(input));

        //Return success response
        return ResponseEntity.status(HttpStatus.OK).body(sampleModel.getInput());
    }

    //TODO: Add more endpoints here




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
