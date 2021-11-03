#! /bin/sh

#Check Java version
echo "Checking Java version..."

java --version >> /dev/null
if [ $? -ne 0 ]
then
	echo "Java not installed."
	exit 1
fi

#Check for necessary folders
echo "Checking for necessary folders..."
ls {conf.d,mysql} >> /dev/null
if [ $? -ne 0 ]
then
	echo "Creating folders..."
	mkdir -p {conf.d,mysql/data}
fi

#Check for the compiled app
echo "Checking for .jar file..."
ls target/*.jar >> /dev/null
if [ $? -ne 0 ]
then
	#Build app
	echo "Building Java application..."
	./mvnw package
fi

#Check for local registry
echo "Checking if a local registry is running..."
docker ps | grep registry
if [ $? -ne 0 ]
then
	docker run -d -p 5000:5000 --name registry registry:2
fi

#Running application stack
echo "Starting containers..."
docker-compose up -d
