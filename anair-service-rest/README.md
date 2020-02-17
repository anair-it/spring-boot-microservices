# BOOTiful Rest Service
Checkout the Restful service  endpoint:
- Publishes a message to Kafka
- Integrates with a gRPC service
- Decorated with Jaeger trace feature
- DockerFile is available to run this service as a docker container
- Run `mvn clean generate-sources` to generate Java classes representing _books.proto_
- Swagger endpoint: `http:\\localhost:8081\anair-service-a\swagger-ui.html`

# Run as a spring boot app
- Add this Spring boot app to Run/Debug configurations in Eclipse/Intellij. 
    - The app should be visible in the Spring boot dashboard as well
- Optionally run through command line: `mvn clean spring-boot:run -P container`


# Run service in K8s
> [Kustomize](https://github.com/kubernetes-sigs/kustomize) only works in k8s >= 1.14

## Create docker image when code changes
```
mvn clean package
docker build -t service-rest:latest .
```

## K8s dashboard
```
cd $WORKSPACE/anair-service-rest/src/main/resources
kubectl apply -f k8s/dashboard-serviceaccount.yaml
kubectl apply -f k8s/dashboard-clusterrole.yaml

# Get token and store it temporarily to login to kubernetes dashboard. Need to this everytime token expires.
kubectl -n kubernetes-dashboard describe secret $(kubectl -n kubernetes-dashboard get secret | grep admin-user | awk '{print $1}')
```

## Base steps
- Create namespace "anair"
````shell script
cd $WORKSPACE/anair-service-rest/src/main/resources
kubectl apply -f k8s/namespace.yaml
````

## Configuration change evolution
- Add new changes to a new directory like "002"
- Add patches in kustomization.yaml "patchesStrategicMerge" section
````shell script
cd $WORKSPACE/anair-service-rest/src/main/resources

# Run kustomize script
kubectl apply -k .

#Enable port forward. Get pod name and replace in below command
kubectl port-forward {{PODNAME}} 8081:8081
````
- Open [Kubernetes dashboard](http://localhost:8001/api/v1/namespaces/kubernetes-dashboard/services/https:kubernetes-dashboard:/proxy/)
- Select namespace "anair" and review components
- Optionally install [K9s command line utility](https://github.com/derailed/k9s) to monitor k8s components

## Accessing service-rest
- Run `kubectl describe svc service-rest-service -n anair`
- Get NodePort
- Go to `http://localhost:NODEPORT/anair-service-rest/actuator/health`