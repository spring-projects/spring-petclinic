# Feast Java Helm Charts (alpha)

This repo contains Helm charts for Feast Java components that are being installed on Kubernetes:
* Feast (root chart): The complete Helm chart containing all Feast components and dependencies. Most users will use this chart, but can selectively enable/disable subcharts using the values.yaml file.
    * [Feature Server](charts/feature-server): High performant JVM-based implementation of feature server.
    * [Transformation Service](charts/transformation-service): Transformation server for calculating on-demand features
    * Redis: (Optional) One of possible options for an online store used by Feature Server
   
## Chart: Feast

Feature store for machine learning Current chart version is `0.28.0`

## Installation

Charts are published to `https://feast-helm-charts.storage.googleapis.com`. Please note that this URL is different from the URL we previously used (`feast-charts`)

Run the following commands to add the repository

```
helm repo add feast-charts https://feast-helm-charts.storage.googleapis.com
helm repo update
```

Install Feast
```
helm install feast-release feast-charts/feast
```

## Customize your installation

This Feast chart comes with a [values.yaml](values.yaml) that allows for configuration and customization of all sub-charts.

In order to modify the default configuration of Feature Server, please use the `application-override.yaml` key in the `values.yaml` file in this chart. A code snippet example
```
feature-server:
    application-override.yaml:
        enabled: true
        feast:
            active_store: online
            stores:
            - name: online
              type: REDIS
              config:
                host: localhost
                port: 6379
            entityKeySerializationVersion: 2

global:
  registry:
    path: gs://[YOUR GCS BUCKET]/demo-repo/registry.db
    cache_ttl_seconds: 60
  project: feast_java_demo

```

For the default configuration, please see the [Feature Server Configuration](https://github.com/feast-dev/feast/blob/master/java/serving/src/main/resources/application.yml).

For more details, please see: https://docs.feast.dev/how-to-guides/running-feast-in-production

## Example
See [here](https://github.com/feast-dev/feast/tree/master/examples/java-demo) for a sample tutorial on testing this helm chart with a demo feature repository and a local Redis instance.

## Requirements

| Repository | Name | Version |
|------------|------|---------|
| https://charts.helm.sh/stable | redis | 10.5.6 |
| https://feast-helm-charts.storage.googleapis.com | feature-server(feature-server) | 0.28.0 |
| https://feast-helm-charts.storage.googleapis.com | transformation-service(transformation-service) | 0.28.0 |

## Values

| Key | Type | Default | Description |
|-----|------|---------|-------------|
| feature-server.enabled | bool | `true` |  |
| global.project | string | `"default"` | Project from feature_store.yaml |
| global.registry | object | `{"cache_ttl_seconds":0,"path":"gs://path/to/registry.db"}` | Information about registry managed by Feast Python SDK (must be in sync with feature_store.yaml) |
| global.registry.cache_ttl_seconds | int | `0` | Registry cache (in memory) will be refreshed on this interval |
| global.registry.path | string | `"gs://path/to/registry.db"` | Path to the registry file managed by Feast Python SDK |
| redis.enabled | bool | `false` | Flag to install Redis |
| redis.usePassword | bool | `false` | Disable redis password |
| transformation-service.enabled | bool | `true` |  |