package org.anair.services.c;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@WebMvcTest
public class ServiceCMainApplicationTest {

    @Autowired
    private MockMvc mvc;

    @Test
    public void contextLoads() {
        ServiceCMainApplication.main(new String[]{});
        assertNotNull(mvc);
    }

}