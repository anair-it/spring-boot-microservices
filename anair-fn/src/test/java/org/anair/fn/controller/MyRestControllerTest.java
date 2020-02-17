package org.anair.fn.controller;

import org.anair.fn.service.MySqsPublisher;
import org.anair.fn.service.S3Service;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import java.nio.file.Path;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@WebMvcTest(MyRestController.class)
@Import(RestControllerErrorHandler.class)
public class MyRestControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private S3Service s3Service;

    @MockBean
    private MySqsPublisher mySqsPublisher;



    @Test
    public void write_200() throws Exception {
        Mockito.when(s3Service.put(Mockito.anyString(), Mockito.any(Path.class))).thenReturn(true);
        Mockito.doNothing().when(mySqsPublisher).sendMessage("msg");

        mockMvc.perform(get("/write/msg"))
                .andExpect(status().isOk())
                .andReturn();

        Mockito.verify(s3Service).put(Mockito.anyString(), Mockito.any(Path.class));
        Mockito.verify(mySqsPublisher).sendMessage("msg");
    }

    @Test
    public void write_400() throws Exception {
        mockMvc.perform(get("/write/ "))
                .andExpect(status().is4xxClientError())
                .andReturn();
    }

    @Test
    public void write_500() {
        Mockito.when(s3Service.put(Mockito.anyString(), Mockito.any(Path.class))).thenReturn(true);
        Mockito.doThrow(new RuntimeException("error")).when(mySqsPublisher).sendMessage("msg");

        Assertions.assertThrows(Exception.class, () -> mockMvc.perform(get("/write/msg"))
                .andExpect(status().is5xxServerError())
                .andReturn());

        Mockito.verify(s3Service).put(Mockito.anyString(), Mockito.any(Path.class));
        Mockito.verify(mySqsPublisher).sendMessage("msg");
    }

}
