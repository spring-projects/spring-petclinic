pipeline {
    agent none
    stages {
       stage('Build') {
           agent {
               docker {
                   image 'maven:3.5.0'
                   args '-e INITIAL_ADMIN_USER -e INITIAL_ADMIN_PASSWORD --network=${LDOP_NETWORK_NAME}'
               }
           }
           steps {
               configFileProvider(
                       [configFile(fileId: 'nexus', variable: 'MAVEN_SETTINGS')]) {
                   sh 'mvn -s $MAVEN_SETTINGS clean deploy -DskipTests=true -B'
               }
           }
       }
       stage('Deploy to Pivotal') {
           agent {
               docker {
                   image 'liatrio/cf-cli'
               }
           }
           steps {
               withCredentials([usernamePassword(credentialsId: 'pivotal', passwordVariable: 'PASSWORD', usernameVariable: 'USERNAME')]){
                 sh "cf api https://api.run.pivotal.io && cf login -u ${env.USERNAME} -p ${env.PASSWORD}"
                 sh 'cf push'
           }
       }
    }
  }
}
