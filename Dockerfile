##Dockerfile for building Spring Pet Clinic app
#
# docker build -t petclininc .

FROM		openjdk:17-jdk-alpine

ENV		MYSQL_USER=petclinic \
		MYSQL_PASS=petclinic \
		MYSQL_URL=jdbc:mysql://database/petclinic \
		DATABASE=h2 \
		JAVA_OPTS=""

COPY		./target/spring-petclinic*.jar /usr/src/myapp/target.jar

WORKDIR		/usr/src/myapp

CMD		java $JAVA_OPTS -Dspring.profiles.active=${DATABASE} -jar /usr/src/myapp/target.jar

EXPOSE		8080
