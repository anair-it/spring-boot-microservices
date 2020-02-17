package org.anair.services.controller;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(SpringExtension.class)
@WebMvcTest(AnairProtoController.class)
public class AnairProtoControllerTest {

    @Autowired
    private MockMvc mockMvc;


    @Test
    public void getBookById_200() throws Exception {
        MvcResult mvcResult = mockMvc.perform(get("/book/1"))
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/x-protobuf;charset=UTF-8"))
                .andReturn();
        MockHttpServletResponse response = mvcResult.getResponse();
        Assertions.assertNotNull(response.getContentAsString());
    }

}
