# BOOTiful Microservices

# Understanding components in this project
## Spring boot parent pom
- anair-parent-pom is the parent pom that manages commonly used dependencies and plugins. 
- All services in this project will extend from this parent pom.
- [Read more...](anair-parent-pom/README.md)

## Spring boot Rest Maven archetype
- Template project that can be used to generate a Restful spring boot application with sample java classes, properties
- [Read more...](anair-rest-archetype/README.md)

## Spring boot services
- [Admin console](anair-service-admin/README.md)
    - Spring boot services admin console
- [Service A](anair-service-a/README.md)
    - Publishes a message on a Kafka topic
    - Invoke a GET grpc endpoint
- [Service B](anair-service-b/README.md)
    - Consumes message from the Kafka topic published by Service A
- [Service C](anair-service-c/README.md)
    - grpc endpoint producing protobuffer content when invoked by Service A

# Running services
## As Docker containers
- Navigate to this project
- Review _docker-compose.yml_ file
- Run `docker-compose up` to start the following services:
    - Jaeger
    - Zookeeper
    - Kafka
    - Spring boot admin
    - Service A
    - Service B
    - Service C
    
## Through IDE as spring boot services
- Start Kafka locally
- [Start Jaeger server](https://www.jaegertracing.io/docs/1.6/getting-started/)
    - Start Jaeger UI. If using a different host and port, update the same _application.properties_ in all services
- Start anair-service-admin application
- Run `mvn clean spring-boot:run` on all services to start it


## UI
- [Jaeger UI](http://localhost:16686)
- [Swagger UI - Service A](http://localhost:8080/anair-service-a/swagger-ui.html)
- [Swagger UI - Service C](http://localhost:8082/anair-service-c/swagger-ui.html)
- [Spring boot admin](http://localhost:18080/admin)

# Executing service endpoints
In Service A swagger page, try out __/publish/{userId}__ endpoint. This will execute a kafka and grpc transaction. Check logs of all 3 services. View the trace graph in Jaeger UI.

 
# Trace graph
Here is the opentrace graph from Jaeger:
![Distributed Trace graph](service_trace.png)