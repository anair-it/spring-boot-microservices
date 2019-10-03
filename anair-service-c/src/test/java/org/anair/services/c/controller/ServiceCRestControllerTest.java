package org.anair.services.c.controller;

import org.hamcrest.Matchers;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@RunWith(SpringRunner.class)
@WebMvcTest(ServiceCRestController.class)
public class ServiceCRestControllerTest {

    @Autowired
    private MockMvc mockMvc;


    @Test
    public void echo_200() throws Exception {
        mockMvc.perform(get("/echo/anair"))
                .andExpect(status().isOk())
                .andExpect(content().string(Matchers.startsWith("anair")))
                .andReturn();
    }

    @Test
    public void getBookById_200() throws Exception {
        MvcResult mvcResult = mockMvc.perform(get("/book/1"))
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/x-protobuf;charset=UTF-8"))
                .andReturn();
        MockHttpServletResponse response = mvcResult.getResponse();
        Assert.assertNotNull(response.getContentAsString());
    }

}
