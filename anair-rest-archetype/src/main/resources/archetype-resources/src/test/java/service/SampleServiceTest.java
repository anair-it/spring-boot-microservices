package ${package}.service;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class SampleServiceTest {

    private SampleServiceImpl sampleService;

    @Before
    public void setup(){
        sampleService = new SampleServiceImpl();
    }

    @Test
    public void sampleMethod_valid_input() {
        String output = sampleService.sampleMethod("hello anair");
        assertEquals("hello anair", output);
    }

    @Test
    public void sampleMethod_null_input() {
        String output = sampleService.sampleMethod(null);
        assertNull(output);
    }
}