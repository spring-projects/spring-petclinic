FROM maven:3.8.1-jdk-11

RUN useradd -m -u 1000 -s /bin/bash jenkins

COPY settings.xml /home/jenkins/.m2/settings.xml

#ENV JAVA_HOME /usr/lib/jvm/jre
#ENV MAVEN_HOME /usr/share/maven
#ENV MAVEN_CONFIG /root/.m2
#ENV MAVEN_OPTS -Dmaven.repo.local=.m2/repository
