package org.anair.services.a.exception;

import org.junit.Test;
import org.slf4j.MDC;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;

import static org.junit.Assert.*;

public class ServiceExceptionTest {

    @Test
    public void serviceException_test(){
        ServiceException e1 = new ServiceException("error message");
        assertNotNull(e1);
        assertEquals("error message", e1.getMessage());

        e1 = new ServiceException(new NullPointerException());
        assertNotNull(e1);
        assertEquals("java.lang.NullPointerException", e1.getCause().getClass().getName());

        e1 = new ServiceException("error message", new NullPointerException());
        assertNotNull(e1);
        assertEquals("error message", e1.getMessage());
        assertEquals("java.lang.NullPointerException", e1.getCause().getClass().getName());
    }
}