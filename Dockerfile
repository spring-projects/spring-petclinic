FROM tomcat:8.0-jre7
MAINTAINER qt@info.com
ADD http://khajasha2.mylabserver.com:8080/job/petclinic/lastSuccessfulBuild/artifact/target/petclinic.war /
EXPOSE 8080
CMD ["catalina.sh", "run"]
