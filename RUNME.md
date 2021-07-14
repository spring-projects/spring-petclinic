# How to run with docker

## To Run it locally

1. Create a docker network: `docker network create my-network`
2. Start a mysql container in the specified network: `docker run -p 3306:3306 --name petclinicdb --net=my-network -e MYSQL_USER=petclinic -e MYSQL_PASSWORD=petclinic -e MYSQL_ROOT_PASSWORD=root -e MYSQL_DATABASE=petclinic mysql:5.7.8`
3. Run the app in the specified network: `docker run -p 8080:8080 --name spring-petclinic --net=my-network -e SPRING_PROFILES_ACTIVE=mysql -e MYSQL_URL='jdbc:mysql://petclinicdb:3306/petclinic' allanweber/spring-petclinic:2.4.5`

## Build image and deploy it

1. Build version: `docker build -t allanweber/spring-petclinic:<version> -f docker/Dockerfile .`
2. Build latest: `docker build -t allanweber/spring-petclinic:latest -f docker/Dockerfile .`
3. Deploy version to registry: `docker push allanweber/spring-petclinic:<version>`
4. Deploy latest to registry: `docker push allanweber/spring-petclinic:latest`

* **Run prd**: docker run -p 8080:8080 --name spring-petclinic -d -e SPRING_PROFILES_ACTIVE=mysql -e MYSQL_URL=0.0.0.0:3306 -e MYSQL_USER=petclinic -e MYSQL_PASS=petclinic allanweber/spring-petclinic:2.4.5


* **Remove local container**: `docker rm -f  spring-petclinic`



docker run -p 8080:8080 --name spring-petclinic --net=my-network -e SPRING_PROFILES_ACTIVE=mysql -e MYSQL_URL='jdbc:mysql://petclinicdb:3306/petclinic' -e MYSQL_USER=petclinic -e MYSQL_PASS=petclinic allanweber/spring-petclinic:2.4.5
