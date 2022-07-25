pipeline {
  agent any
  stages {
    stage('init') {
      steps {
        checkout scm
      }
    }


stage('Scan') {
      steps {
        withSonarQubeEnv(installationName: 'sq') { 
          sh './mvnw clean install org.sonarsource.scanner.maven:sonar-maven-plugin:3.9.0.2155:sonar'
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
