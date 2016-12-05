FROM tomcat:8.0-jre7
MAINTAINER qt@info.com
ADD petclinic.war /
EXPOSE 8080
CMD ["catalina.sh", "run"]
