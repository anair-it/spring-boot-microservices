# Metrics
- Prometheus is a time-series database that is used to capture application and system metrics
- Grafana is the visualization on top of prometheus data accessible through promQL

# Configuration
## Prometheus
- Review _prometheus/prometheus.yml_
- Add/update jobs and scrape interval as required. By default anair microservices and docker jobs are already in there.

## Grafana
- JVM dashboard is at _grafana/provisioning/dashboards/jvm-micrometer.json_
- A default Prometheus dashboard provider configuration is at _grafana/provisioning/dashboards/dashboard.yml_
    - This points ot the above prometheus installation

# Urls
- [Prometheus](http://localhost:9090)
- [Grafana](https://localhost:3000)
    - Login as admin/admin
    
# Dashboard
A default JVM Grafana dashboard is provided
