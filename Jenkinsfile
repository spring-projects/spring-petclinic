pipeline {
  agent any
  stages {
    stage('init') {
      steps {
        checkout scm
      }
    }
    stage('Build project') {
      steps {
        sh 'mvn clean install'
      }
    }


stage('Scan') {
      steps {
        withSonarQubeEnv(installationName: 'sq') { 
          sh './mvnw clean install org.sonarsource.scanner.maven:sonar-maven-plugin:3.9.0.2155:sonar'
        }
      }
    }
 
    stage('SQuality Gate') {
      steps {
        timeout(time: 1, unit: 'MINUTES') {
          waitForQualityGate abortPipeline: true
        }
      }
    }
    
   stage('Package') {
      steps {
        sh 'mvn package'
      }
    }
    
   stage('Deploy') {
      steps {
        sh 'java -jar -Dserver.port=8083 target/spring-petclinic-2.7.0-SNAPSHOT.jar'
      }
    }

  }
  tools {
    maven 'maven'
    jdk 'java11'
  }

  }
