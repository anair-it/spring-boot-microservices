package org.anair.services.b;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@WebMvcTest
public class ServiceBApplicationTest {

    @Test
    public void contextLoads() {
        ServiceBApplication.main(new String[]{});
    }

}