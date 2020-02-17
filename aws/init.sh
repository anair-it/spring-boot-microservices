#!/bin/bash
aws --endpoint-url http://localhost:4572 s3 mb s3://anair-bucket

aws --endpoint-url http://localhost:4576 sqs create-queue --queue-name anair-request-dlq
aws --endpoint-url http://localhost:4576 sqs create-queue --queue-name anair-request-q --attributes '{"RedrivePolicy": "{\"deadLetterTargetArn\":\"arn:aws:sqs:us-east-1:000000000000:anair-request-dlq\",\"maxReceiveCount\":5}"}'
aws --endpoint-url http://localhost:4576 sqs create-queue --queue-name anair-response-q