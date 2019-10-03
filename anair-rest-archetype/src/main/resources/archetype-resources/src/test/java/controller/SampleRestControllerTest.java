#set( $symbol_pound = '#' )
#set( $symbol_dollar = '$' )
#set( $symbol_escape = '\' )
package ${package}.controller;

import org.hamcrest.Matchers;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@WebMvcTest(SampleRestController.class)
public class SampleRestControllerTest {

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
    public void sample_200() throws Exception {
        mockMvc.perform(get("/sample/anair"))
                .andExpect(status().isOk())
                .andExpect(content().string("anair"))
                .andReturn();
    }

    @Test
    public void sample_400() throws Exception {
        mockMvc.perform(get("/sample"))
                .andExpect(status().is4xxClientError())
                .andReturn();
    }
}
