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
- [Service A](anair-service-a/README.md)
- [Service B](anair-service-b/README.md)
- [Service C](anair-service-c/README.md)


# Starting services
- Start Kafka locally
- [Start Jaeger server](https://www.jaegertracing.io/docs/1.6/getting-started/)
- Start all 3 spring boot services

## Swagger UI
- [Service A](http://localhost:8080/anair-service-a/swagger-ui.html)
- [Service C](http://localhost:8082/anair-service-c/swagger-ui.html)

# Executing service endpoints
In Service A swagger page, try out __/publish/{userId}__ endpoint. This will execute a kafka and gRPC transaction. Check logs of all 3 services. View the trace graph below.

 
# Trace graph
Here is the opentrace graph from Jaeger:
![Distributed Trace graph](service_trace.png)