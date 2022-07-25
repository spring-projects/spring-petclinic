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
        sh 'mvn install'
      }
    }

stage('Scan') {
      steps {
        withSonarQubeEnv(installationName: 'sq1') { 
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
  environment {
    GIT_COMMIT_SHORT = sh(
                                   script: "printf \$(git rev-parse --short ${GIT_COMMIT})",
                                   returnStdout: true
                                  )
    }
  }
