package org.anair.services.controller;

import org.anair.services.exception.ServiceException;
import org.anair.services.service.PublishService;
import org.anair.services.protobuf.Books;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.client.RestTemplate;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@WebMvcTest(AnairRestController.class)
@Import(RestControllerErrorHandler.class)
public class AnairRestControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private PublishService publishService;

    @MockBean
    private RestTemplate restTemplate;


    @Test
    public void echo_200() throws Exception {
        mockMvc.perform(get("/echo/anair"))
                .andExpect(status().isOk())
                .andExpect(content().string(Matchers.startsWith("anair")))
                .andReturn();
    }

    @Test
    public void publish_200() throws Exception {
        ResponseEntity bookEntity = Mockito.mock(ResponseEntity.class);
        Mockito.doNothing().when(publishService).publish("anair");
        Mockito.when(bookEntity.getStatusCode()).thenReturn(HttpStatus.OK);

        Books.Book book = Books.Book.newBuilder()
                .setId(1)
                .setName("Book1")
                .addAuthor(Books.Author.newBuilder()
                        .setId(0)
                        .setFirstName("fname1")
                        .setLastName("lname1"))
                .build();
        Mockito.when(bookEntity.getBody()).thenReturn(book);
        Mockito.when(restTemplate.getForEntity("http://localhost:8083/anair-service-proto/book/1", Books.Book.class)).thenReturn(bookEntity);

        mockMvc.perform(get("/publish/anair"))
                .andExpect(status().isOk())
                .andReturn();

        Mockito.verify(publishService).publish("anair");
        Mockito.verify(restTemplate).getForEntity("http://localhost:8083/anair-service-proto/book/1", Books.Book.class);
        Mockito.verify(bookEntity).getStatusCode();
        Mockito.verify(bookEntity).getBody();
    }

    @Test
    public void publish_kafka_error_500() throws Exception {
        Mockito.doThrow(new ServiceException()).when(publishService).publish("anair");
        mockMvc.perform(get("/publish/anair"))
                .andExpect(status().is5xxServerError())
                .andReturn();
        Mockito.verify(publishService).publish("anair");
    }

    @Test
    public void publish_grpc_error_500() throws Exception {
        ResponseEntity bookEntity = Mockito.mock(ResponseEntity.class);
        Mockito.doNothing().when(publishService).publish("anair");
        Mockito.when(bookEntity.getStatusCode()).thenReturn(HttpStatus.INTERNAL_SERVER_ERROR);
        Mockito.when(restTemplate.getForEntity("http://localhost:8083/anair-service-proto/book/1", Books.Book.class)).thenReturn(bookEntity);

        mockMvc.perform(get("/publish/anair"))
                .andExpect(status().is5xxServerError())
                .andReturn();

        Mockito.verify(publishService).publish("anair");
        Mockito.verify(restTemplate).getForEntity("http://localhost:8083/anair-service-proto/book/1", Books.Book.class);
        Mockito.verify(bookEntity).getStatusCode();
    }

    @Test
    public void publish_400() throws Exception {
        mockMvc.perform(get("/publish"))
                .andExpect(status().is4xxClientError())
                .andReturn();
    }
}
