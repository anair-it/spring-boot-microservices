package org.anair.fn;


import com.amazonaws.serverless.exceptions.ContainerInitializationException;
import com.amazonaws.serverless.proxy.internal.LambdaContainerHandler;
import com.amazonaws.serverless.proxy.model.*;
import com.amazonaws.serverless.proxy.spring.SpringBootLambdaContainerHandler;
import com.amazonaws.serverless.proxy.spring.SpringBootProxyHandlerBuilder;
import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestStreamHandler;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.opentracing.Tracer;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.MapUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.util.StopWatch;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.time.Instant;
import java.util.List;
import java.util.Map;

/**
 * Process Cloud Watch, SQS events and API gateway events
 */
@Slf4j
public class MyLambdaHandler implements RequestStreamHandler {
    private static final String SQS_SOURCE_AWS_EVENT = "aws:sqs";
    private static final String CLOUDWATCH_SOURCE_AWS_EVENT = "aws.events";

    private static SpringBootLambdaContainerHandler<AwsProxyRequest, AwsProxyResponse> handler;
    private static ObjectMapper mapper = LambdaContainerHandler.getObjectMapper();
    private Tracer tracer;

    @Override
    public void handleRequest(InputStream inputStream, OutputStream outputStream, Context context) {
        StopWatch sw = null;
        if (log.isDebugEnabled()) {
            sw = new StopWatch("lambda-request");
            sw.start();
        }

        initLambdaHandler();
        processEvent(inputStream, outputStream, context);

        if(log.isDebugEnabled() && sw != null) {
            sw.stop();
            log.debug("Lambda request -> {} sec", sw.getTotalTimeSeconds());
        }
    }

    private static void initLambdaHandler() {
        if(handler == null) {
            try {
                SpringBootProxyHandlerBuilder springBootProxyHandlerBuilder = new SpringBootProxyHandlerBuilder()
                        .defaultProxy()
                        .asyncInit(Instant.now().toEpochMilli())
                        .springBootApplication(MyApplication.class);

                String profiles = System.getenv("profile");
                if(StringUtils.isNotEmpty(profiles)) {
                    String[] profileArr = StringUtils.splitPreserveAllTokens(profiles, ',');
                    springBootProxyHandlerBuilder.profiles(profileArr);
                }

                handler = springBootProxyHandlerBuilder.buildAndInitialize();
            } catch (ContainerInitializationException e) {
                throw new RuntimeException("Could not initialize Spring Boot application", ExceptionUtils.getRootCause(e));
            }
        }
    }

    private void processEvent(InputStream inputStream, OutputStream outputStream, Context context) {
        try {
            String event = readEvent(inputStream);

            if (StringUtils.isNotEmpty(event)) {
                AwsProxyRequest request = mapper.readValue(event, AwsProxyRequest.class);
                if (StringUtils.isEmpty(request.getHttpMethod())) { //Not http request. SQS event
                    Map<String, Object> raw = (Map<String, Object>) mapper.readValue(event, Map.class);

                    resolveEventSource(outputStream, context, raw);
                } else { //Http request
                    outputAwsProxyResponse(outputStream, context, mapper, request);
                }
            }
        }catch (Exception e){
            throw new RuntimeException(e.getMessage(), e);
        }
    }

    private void resolveEventSource(OutputStream outputStream, Context context, Map<String, Object> raw) throws IOException {
        AwsProxyRequest request;
        if (MapUtils.isNotEmpty(raw)) {
            String eventSource = (String) raw.get("source");

            if (StringUtils.isEmpty(eventSource)) {
                resolveSQSEventSource(outputStream, context, raw);
            } else if (eventSource.equals(CLOUDWATCH_SOURCE_AWS_EVENT)) {
                log.trace("Cloud watch event");
                request = createHttpRequest("body for post");
                log.info("Converted Cloud watch event to http request {}", request.getPath());
                outputAwsProxyResponse(outputStream, context, mapper, request);
            }
        }
    }

    private void resolveSQSEventSource(OutputStream outputStream, Context context, Map<String, Object> raw) throws IOException {
        if (MapUtils.isNotEmpty(raw)) {
            List<?> records = (List<?>)raw.get("Records");
            if(CollectionUtils.isNotEmpty(records)){
                log.trace("Processing {} SQS events", records.size());
                records.forEach(record -> {
                    Map<String, Object> sqsEventRecord = (Map<String, Object>) record;
                    if(MapUtils.isNotEmpty(sqsEventRecord)) {
                        String sqsEventSource = (String) sqsEventRecord.get("eventSource");
                        if (sqsEventSource.equals(SQS_SOURCE_AWS_EVENT)) {
                            String message = (String) sqsEventRecord.get("body");
                            if(StringUtils.isNotBlank(message)) {
                                AwsProxyRequest request = createHttpRequest(message);
                                log.info("Converted SQS event to http request {}", request.getPath());
                                try {
                                    outputAwsProxyResponse(outputStream, context, mapper, request);
                                } catch (Exception e) {
                                    log.error("Failed to create AwsProxyResponse for the SQS record: {}", sqsEventRecord.toString(), ExceptionUtils.getRootCause(e));
                                }
                            }else{
                                log.error("Empty message body. Will not be processed");
                            }
                        }
                    }else{
                        log.error("Empty SQS event record");
                    }
                });
                log.info("Processed {} SQS events", records.size());
            }
        }
    }

    private String readEvent(InputStream inputStream) {
        String event = null;
        try {
            event = IOUtils.toString(inputStream, LambdaContainerHandler.getContainerConfig().getDefaultContentCharset());
            log.trace("SQS Event: {}", event);
        } catch (IOException e) {
            log.error(e.getMessage(), e);
        } finally{
            if(inputStream != null){
                try {
                    inputStream.close();
                } catch (IOException e) {
                    log.error("Failed to close event input stream", ExceptionUtils.getRootCause(e));
                }
            }
        }
        return event;
    }

    private void outputAwsProxyResponse(OutputStream outputStream, Context context, ObjectMapper mapper, AwsProxyRequest request) throws IOException {
        AwsProxyResponse response = handler.proxy(request, context);
        mapper.writeValue(outputStream, response);
        outputStream.close();
    }

    private static AwsProxyRequest createHttpRequest(String content) {
        AwsProxyRequest request = new AwsProxyRequest();
        request.setHttpMethod(HttpMethod.GET.name());
        request.getMultiValueHeaders().add(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE);
        request.setPath("/write/"+content);

        AwsProxyRequestContext rc = new AwsProxyRequestContext();
        rc.setIdentity(new ApiGatewayRequestIdentity());
        request.setRequestContext(rc);

        return request;
    }
}