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
          sh './mvnw clean org.sonarsource.scanner.maven:sonar-maven-plugin:3.9.0.2155:sonar'
        }
      }
    }
 
    stage('SQuality Gate') {
      steps {
        timeout(time: 1, unit: 'MINUTES') {
          waitForQualityGate true
        }
      }
    }

  }
  tools {
    maven 'maven'
    jdk 'java11'
  }

  }
