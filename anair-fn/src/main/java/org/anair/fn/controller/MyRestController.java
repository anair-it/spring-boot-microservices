package org.anair.fn.controller;

import lombok.extern.slf4j.Slf4j;
import org.anair.fn.service.MySqsPublisher;
import org.anair.fn.service.S3Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.NotBlank;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;


/**
 * Controller for anair fn
 */
@RestController
@Validated
@Slf4j
public class MyRestController {

    @Autowired
    private S3Service s3Service;

    @Autowired
    private MySqsPublisher mySqsPublisher;


    @GetMapping(value = "/write/{content}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> publish(@NotBlank @PathVariable("content") final String content) throws IOException {
        Path tempFile = Files.createTempFile("org", "anair");
        Files.write(tempFile, content.getBytes());
        tempFile.toFile().deleteOnExit();
        s3Service.put("anair-key", tempFile);

        mySqsPublisher.sendMessage(content);

        return ResponseEntity.status(HttpStatus.OK).build();
    }

}
