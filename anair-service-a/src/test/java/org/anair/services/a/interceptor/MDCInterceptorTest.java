package org.anair.services.a.interceptor;

import org.junit.Test;
import org.slf4j.MDC;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;

import static org.junit.Assert.*;

public class MDCInterceptorTest {

    @Test
    public void validate(){
        MDCInterceptor mdcInterceptor = new MDCInterceptor();
        MockHttpServletRequest req = new MockHttpServletRequest();
        assertTrue(mdcInterceptor.preHandle(req, new MockHttpServletResponse(), new Object()));
        mdcInterceptor.afterCompletion(new MockHttpServletRequest(), new MockHttpServletResponse(), new Object(), null);
        assertNull(MDC.get("hello"));
    }
}