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

    stage('SonarQube analysis') {
      environment {
        SCANNER_HOME = 'Sonar-scanner'
      }
      steps {
        withSonarQubeEnv(credentialsId: 'sonar-credentialsId', installationName: 'Sonar') {
          sh '$SCANNER_HOME/bin/sonar-scanner          -Dsonar.projectKey=projectKey          -Dsonar.projectName=projectName          -Dsonar.sources=src/          -Dsonar.java.binaries=target/classes/          -Dsonar.exclusions=src/test/java/****/*.java          -Dsonar.java.libraries=/var/lib/jenkins/.m2/**/*.jar          -Dsonar.projectVersion=${BUILD_NUMBER}-${GIT_COMMIT_SHORT}'
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