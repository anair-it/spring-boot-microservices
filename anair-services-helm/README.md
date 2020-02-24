# Pre-requisite
- Kubernetes is running locally
- Host docker is connected to th eK8s VM docker daemon
- Docker image is created for anair-service-rest
- K8s namespace anair is created (`kubectl create namespace anair`)

# Install
- Create configmap from application.properties: `kubectl create configmap anair-service-rest-cm --from-file=../anair-service-rest/src/main/resources/application.properties -n anair`
- Helm commands:
```
    # Lint helm script
    helm init .
    
    # Install
    helm install anoop . -n anair
```

# Verify
- Verify pod is created and running: `kubectl get po -n anair`
- Get the application URL by running these commands:
```    
    export NODE_PORT=$(kubectl get --namespace anair -o jsonpath="{.spec.ports[0].nodePort}" services anoop-anair-services-helm)
    export NODE_IP=$(kubectl get nodes --namespace anair -o jsonpath="{.items[0].status.addresses[0].address}")
    echo http://$NODE_IP:$NODE_PORT
```
- Swagger page: __http://$NODE_IP:$NODE_PORT/anair-service-rest/swagger-ui.html__