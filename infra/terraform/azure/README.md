# Terraform config for Feast on Azure

This serves as a guide on how to deploy Feast on Azure. At the end of this guide, we will have provisioned:
1. AKS cluster
2. Feast services running on AKS
3. Azure Cache (Redis) as online store
4. Spark operator on AKS
5. Kafka running on HDInsight.

# Steps

1. Create a tfvars file, e.g. `my.tfvars`. A sample configuration is as below:

```
name_prefix             = "feast09"
resource_group          = "Feast" # pre-exisiting resource group
```

3. Configure tf state backend, e.g.:
```
terraform {
  backend "azurerm" {
    storage_account_name = "<your storage account name>"
    container_name       = "<your container name>"
    key                  = "<your blob name>"
  }
}
```

3. Use `terraform apply -var-file="my.tfvars"` to deploy.

Note: to get the list of Kafka brokers needed for streaming ingestion, use

`curl -sS -u <Kafka gateway username>:<Kafka gateway password> -G https://<Kafka cluster name>.azurehdinsight.net/api/v1/clusters/<Kafka cluster name>/services/KAFKA/components/KAFKA_BROKER | jq -r '["\(.host_components[].HostRoles.host_name):9092"] | join(",")'`

where the Kafka gateway username is <name_prefix>-kafka-gateway, the Kafka cluster name is <name_prefix>-kafka, and the Kafka gateway password is a kubectl secret under the name feast-kafka-gateway.
