package org.anair.services.interceptor;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.slf4j.MDC;

import java.util.HashMap;
import java.util.Map;

public class RequestPayloadLoggingAdviceTest {


    @Test
    public void supports() {
        RequestPayloadLoggingAdvice advice = new RequestPayloadLoggingAdvice();
        Assertions.assertTrue(advice.supports(null, null, null));
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
        Assertions.assertNotNull(obj);
        Assertions.assertEquals("type1", MDC.get("app-type"));
        Assertions.assertEquals("http", MDC.get("request-protocol"));
    }

    @Test
    public void afterBodyRead_empty_map() {
        RequestPayloadLoggingAdvice advice = new RequestPayloadLoggingAdvice();

        Object obj = advice.afterBodyRead(new HashMap(), null, null, null, null);
        Assertions.assertNotNull(obj);
        Assertions.assertNull(MDC.get("app-type"));
        Assertions.assertNull(MDC.get("request-protocol"));
    }

    @Test
    public void afterBodyRead_not_map() {
        RequestPayloadLoggingAdvice advice = new RequestPayloadLoggingAdvice();

        Object obj = advice.afterBodyRead("invalid", null, null, null, null);
        Assertions.assertNotNull(obj);
        Assertions.assertNull(MDC.get("app-type"));
        Assertions.assertNull(MDC.get("request-protocol"));
    }
}