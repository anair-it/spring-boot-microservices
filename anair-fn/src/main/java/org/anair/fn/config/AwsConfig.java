package org.anair.fn.config;


import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.http.urlconnection.UrlConnectionHttpClient;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.S3ClientBuilder;
import software.amazon.awssdk.services.sqs.SqsClient;
import software.amazon.awssdk.services.sqs.SqsClientBuilder;

import java.net.URI;
import java.net.URISyntaxException;

@Configuration
public class AwsConfig {

    @Value("${aws.accesskey}")
    private String accessKey;

    @Value("${aws.secretkey}")
    private String secretKey;

    @Value("${aws.s3.endpoint}")
    private String s3Endpoint;

    @Value("${aws.sqs.endpoint}")
    private String sqsEndpoint;

    @Value("${aws.region:us-east-1}")
    private String region;



    @Bean
    public S3Client s3() throws URISyntaxException {
        S3ClientBuilder s3ClientBuilder = S3Client.builder()
                .region(Region.of(region))
                .httpClient(UrlConnectionHttpClient.builder().build());

        if(StringUtils.isNotEmpty(accessKey) && StringUtils.isNotEmpty(secretKey)) {
            s3ClientBuilder.credentialsProvider(() -> AwsBasicCredentials.create(this.accessKey, this.secretKey))
                    .endpointOverride(new URI(s3Endpoint));
        }

        return s3ClientBuilder.build();
    }

    @Bean
    public SqsClient sqs() throws URISyntaxException {
        SqsClientBuilder sqsClientBuilder = SqsClient.builder()
                .region(Region.of(region))
                .httpClient(UrlConnectionHttpClient.builder().build());

        if(StringUtils.isNotEmpty(accessKey) && StringUtils.isNotEmpty(secretKey)) {
            sqsClientBuilder.credentialsProvider(() -> AwsBasicCredentials.create(this.accessKey, this.secretKey))
                    .endpointOverride(new URI(sqsEndpoint));
        }

        return sqsClientBuilder.build();
    }

}
