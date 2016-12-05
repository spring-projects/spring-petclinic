FROM tomcat:8.0-jre7
MAINTAINER qt@info.com
ADD http://bit.ly/2h9JXRj /usr/local/tomcat/webapps/
EXPOSE 8080
CMD ["catalina.sh", "run"]
