# BOOTiful AWS Lambda Function
AWS lambda function implemented in Java 11 and Spring boot 2
Write content to a S3 file and publish the content to an outbound SQS queue

# Run as a spring boot app
- Add this Spring boot app to Run/Debug configurations in Eclipse/Intellij. 
    - The app should be visible in the Spring boot dashboard as well
- Optionally run through command line: `mvn clean spring-boot:run -P container`

# Run as a AWS Lambda FaaS
```
## Package lambda zip
mvn clean package -P lambda
```
- _org.anair.fn.MyLambdaHandler_ is the Lambda starter class. This class is configured to process events from Cloud watch, SQS and API gateway which will inturn call
an internal rest endpoint _GET /write_
- Jenkins file has the option to select lambda profile.

## Run in localstack
- Ensure localstack (http://localhost:8088) is running and all required objects are created before following below steps.

```
# Create function in localstack
aws --endpoint-url http://localhost:4574 lambda create-function --function-name MyFn --handler org.anair.fn.MyLambdaHandler --memory-size 512 --zip-file fileb://target/anair-fn-0.0.1-SNAPSHOT-local-0-lambda.zip --runtime java8 --role AWSLambdaBasicExecutionRole --environment "Variables={profile=local}"

# Verify function is deployed
aws --endpoint-url http://localhost:4574 lambda get-function --function-name MyFn

# Create SQS event source
aws --endpoint-url=http://localhost:4574 lambda create-event-source-mapping --function-name MyFn --batch-size 10 --event-source-arn arn:aws:sqs:us-east-1:000000000000:anair-request-q

# Send message to request Q that will trigger MyFn. Verify logs at `docker logs -f localstack`
aws --endpoint-url http://localhost:4576 sqs send-message --queue-url http://localhost:4576/queue/anair-request-q --message-body 'anair'

# Invoke function directly with a scheduled event payload. Verify logs at `docker logs -f localstack`
aws --endpoint-url=http://localhost:4574 lambda invoke --function-name MyFn --invocation-type Event --payload '{"id": "cdc73f9d-aea9-11e3-9d5a-835b769c0d9c","detail-type": "Scheduled Event","source": "aws.events","account": "123456789012","time": "1970-01-01T00:00:00Z","region": "{region}","resources": ["arn:{partition}:events:{region}:123456789012:rule/my-schedule"],"detail": {}}' response.json

# Update function after code change
aws --endpoint-url=http://localhost:4574 lambda update-function-code --function-name MyFn --zip-file fileb://target/anair-fn-0.0.1-SNAPSHOT-local-0-lambda.zip

# Delete function
aws --endpoint-url=http://localhost:4574 lambda delete-function --function-name MyFn

```

## Accessing anair-fn
- Go to `http://localhost:8089/anair-fn/actuator/health`