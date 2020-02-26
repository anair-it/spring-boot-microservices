# Pre-requisite
- Kubernetes is running locally
- Host docker is connected to th eK8s VM docker daemon
- Docker image is created for anair-service-rest
- K8s namespace anair is created (`kubectl create namespace anair`)

# Install
- Helm commands:
```shell script
# Lint helm script
helm lint .

# Install
helm install demo . -n anair

# Verify
helm ls -a -n aap
helm history demo -n anair

# Upgrade with new changes
helm upgrade demo . -n anair

# Rollback version 2
helm rollback demo 2 -n anair 
```
- Enable port forward. Get pod name and replace in: `kubectl port-forward {{PODNAME}} 8080:8080 -n anair`

# Verify
- Verify pod is created and running: `kubectl get all -n anair`
- Open Kubernetes dashboard
    - Select namespace "anair" and review components
- Go to `http://localhost:8080/anair-service-rest/actuator/health`
- Go to `http://localhost:8080/anair-service-rest/swagger-ui.html`
