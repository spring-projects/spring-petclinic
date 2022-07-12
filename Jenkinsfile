#!groovy

pipeline {
  agent any
  stages {
 
     stage("Git Clone"){
       steps{
        git credentialsId: '', url: 'https://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@github.com/stefanmucha/spring-petclinic'
       }
    }

     stage('Gradle Build') {
      steps{
        sshagent(['git'])
        script{
          try{
          sh './gradlew build'
          }finally
          {}
        }
        }
    }

    
    
    stage('Docker Build') {
      agent any
      steps {
        sh 'docker build --no-cache -t muchast2/spring-petclinic:latest .'
       // sh 'docker stop muchast2/spring-petclinic:latest'
        sh 'docker run -p 8081:8080 muchast2/spring-petclinic:latest &'
        sh 'docker images --quiet --filter=dangling=true | xargs --no-run-if-empty docker rmi'
      }
    }
    
}
}
