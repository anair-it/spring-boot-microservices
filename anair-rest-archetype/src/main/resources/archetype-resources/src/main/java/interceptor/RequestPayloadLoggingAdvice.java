#set( $symbol_pound = '#' )
#set( $symbol_dollar = '$' )
#set( $symbol_escape = '\' )
package ${package}.interceptor;

import org.apache.commons.collections4.MapUtils;
import org.slf4j.MDC;
import org.springframework.core.MethodParameter;
import org.springframework.http.HttpInputMessage;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.RequestBodyAdviceAdapter;

import java.lang.reflect.Type;
import java.util.Map;

/**
 * Use this to intercept only POST/PUT requests
 * <p>If there is a need to capture Http request values, autowire HttpServletRequest and use it as required</p>
 */
@ControllerAdvice
public class RequestPayloadLoggingAdvice extends RequestBodyAdviceAdapter {

    @Override
    public Object afterBodyRead(Object body, HttpInputMessage inputMessage, MethodParameter parameter, Type targetType,
            Class<? extends HttpMessageConverter<?>> converterType) {

        MDC.clear();
        if(body instanceof Map){
            Map<String, Object> bodyMap = (Map<String, Object>) body;
            if (MapUtils.isNotEmpty(bodyMap)) {
                Map<String, String> payloadMap = (Map<String, String>) bodyMap.get("payload");
                if (MapUtils.isNotEmpty(payloadMap))
                    payloadMap.forEach(MDC::put);
            }
        }
        return super.afterBodyRead(body, inputMessage, parameter, targetType, converterType);
    }

    @Override
    public boolean supports(MethodParameter methodParameter, Type type, Class<? extends HttpMessageConverter<?>> aClass) {
        return true;
    }
}