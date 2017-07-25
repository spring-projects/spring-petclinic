pipeline {
    agent none
    stages {
       stage('Build') {
           agent {
               docker {
                   image 'maven:3.5.0'
                   args '--network=pipelinedeveloper_default'
               }
           }
           steps {
               configFileProvider(
                       [configFile(fileId: 'nexus', variable: 'MAVEN_SETTINGS')]) {
                   sh 'mvn -s $MAVEN_SETTINGS clean deploy -DskipTests=true -B'
               }
           }
       }
       stage('Build container') {
             agent any
             steps {
                 sh 'docker build -t petclinic-tomcat .'
             }
         }
         stage('Deploy container locally') {
             agent any
             steps {
                 sh 'docker rm -f petclinic-tomcat-temp || true'
                 sh 'docker run -p 18887:8080 -d --name petclinic-tomcat-temp petclinic-tomcat'
                 echo 'Should be available at http://localhost:18887/petclinic/'
             }
         }
         stage('Stop Container') {
             agent any
             steps {
                 input 'Stop container?'
                 sh 'docker rm -f petclinic-tomcat-temp || true'
             }
         }
    }
}
