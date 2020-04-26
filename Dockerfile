FROM ubuntu
RUN apt-get update -y && apt-get install unzip && apt-get install wget -y
RUN wget https://binaries.sonarsource.com/Distribution/sonar-scanner-cli/sonar-scanner-cli-4.2.0.1873-linux.zip && unzip sonar-scanner-cli-4.2.0.1873-linux.zip
#WORKDIR /sonar-scanner-cli-4.2.0.1873-linux/bin
ADD spring-petclinic /opt
RUN echo "sonar.host.url=http://3.23.102.180:9000" >> /sonar-scanner-4.2.0.1873-linux/conf/sonar-scanner.properties
WORKDIR /sonar-scanner-4.2.0.1873-linux/bin
RUN ./sonar-scanner -Dsonar.projectKey=sonar  -Dsonar.sources=/opt/src  -Dsonar.projectVersion=1 -Dsonar.sources=/opt/.
