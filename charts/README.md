# Using helm to install petclinic

An example [helm](https://helm.sh/) chart is included that will install this application. The mysql database is set through the spring profile argument to CMD in the Dockerfile so before deploying the application using helm, a copy of mysql needs to running with the service name of `mysql-mydb` exposed in the cluster. Note: the mysql db chart init container needs to run as root, so if you are using a system with pod security policies, the namespace for the deployment needs to allow pods with this priviledge. For IBM Cloud private, the minimum namespace pod-security policy needed is `ibm-anyund-psp`.

## Installing

Properties for connecting to the database are set in `src/main/resources/application-mysql.properties` To start a mysql database in your cluster matching these properties use (enable --tls for IBM Cloud Private 3.1.x):

```
helm install --name mydb --set mysqlRootPassword=petclinic,mysqlDatabase=petclinic stable/mysql [--tls]
```

Verify that the database is running before starting the Petclinic application. Once the database is active, deploy petclinic from the charts folder using:

```
helm install --name petclinic petclinic [--tls]
```

If your kubernetes cluster has an ingress controller at `my.ip.v4.address`, you can alternatively deploy the application to use this ingress using:

```
helm install --name petclinic --set ingress.enabled=true,ingress.hosts={petclinic.my.ip.v4.address.xip.io} petclinic [--tls]
```

## Cleaning up

To remove the deployed application:

```
helm delete petclinic --purge [--tls]
```

To remove the deployed mysql instance

```
helm delete mydb --purge [--tls]
```