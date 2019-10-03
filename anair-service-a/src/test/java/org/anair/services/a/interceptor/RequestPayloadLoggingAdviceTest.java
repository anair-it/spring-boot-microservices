package org.anair.services.a.interceptor;

import org.junit.Test;
import org.slf4j.MDC;

import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.*;

public class RequestPayloadLoggingAdviceTest {


    @Test
    public void supports() {
        RequestPayloadLoggingAdvice advice = new RequestPayloadLoggingAdvice();
        assertTrue(advice.supports(null, null, null));
    }

    @Test
    public void afterBodyRead_full_payload() {
        Map<String, String> payloadMap = new HashMap<>();
        payloadMap.put("app-type", "type1");
        payloadMap.put("request-protocol", "http");

        Map<String, Object> bodyMap = new HashMap<>();
        bodyMap.put("payload", payloadMap);

        RequestPayloadLoggingAdvice advice = new RequestPayloadLoggingAdvice();

        Object obj = advice.afterBodyRead(bodyMap, null, null, null, null);
        assertNotNull(obj);
        assertEquals("type1", MDC.get("app-type"));
        assertEquals("http", MDC.get("request-protocol"));
    }

    @Test
    public void afterBodyRead_empty_map() {
        RequestPayloadLoggingAdvice advice = new RequestPayloadLoggingAdvice();

        Object obj = advice.afterBodyRead(new HashMap(), null, null, null, null);
        assertNotNull(obj);
        assertNull(MDC.get("app-type"));
        assertNull(MDC.get("request-protocol"));
    }

    @Test
    public void afterBodyRead_not_map() {
        RequestPayloadLoggingAdvice advice = new RequestPayloadLoggingAdvice();

        Object obj = advice.afterBodyRead("invalid", null, null, null, null);
        assertNotNull(obj);
        assertNull(MDC.get("app-type"));
        assertNull(MDC.get("request-protocol"));
    }
}