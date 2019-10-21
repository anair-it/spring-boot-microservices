# Metrics
- Prometheus is a time-series database that is used to capture application and system metrics
- Grafana is the visualization on top of prometheus data accessible through promQL

# Configuration
## Prometheus
- Review _prometheus/prometheus.yml_
- Add/update jobs and scrape interval as required. By default anair microservices and docker jobs are already in there.

## Loki
Visualize logs in Grafana.    
- Refer config file: _loki/conf/loki-config.yaml_

## Grafana
- JVM dashboard is at _grafana/provisioning/dashboards/jvm-micrometer.json_
- A default Prometheus dashboard provider configuration is at _grafana/provisioning/dashboards/dashboard.yml_
    - This points to the above prometheus installation
- Add Loki datasource at http://loki:3100

# Urls
- [Prometheus](http://localhost:9090)
- [Grafana](https://localhost:3000)
    - Login initially as admin/admin. If asked to enter new password, do so and note it down.
- [cAdvisor](http://localhost:28080)
- [Loki healthcheck](http://localhost:3100/ready)
- [Loki metrics](http://localhost:3100/metrics)
    
# Dashboard
A default JVM Grafana dashboard is provided
