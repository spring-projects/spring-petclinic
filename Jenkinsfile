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
        sh 'mvn clean'
      }
    }

    stage('Scan') {
      steps {
        withSonarQubeEnv('sq') {
          sh './mvnw clean install org.sonarsource.scanner.maven:sonar-maven-plugin:3.9.0.2155:sonar'
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

    stage('Package') {
      steps {
        sh 'mvn package'
      }
    }

    stage('Move JAR file') {
      steps {
        sh 'sudo mkdir -p /home/ubuntu/petclinic-deploy/'
        sh 'sudo cp target/spring-petclinic-2.7.0-SNAPSHOT.jar /home/ubuntu/petclinic-deploy/'
      }
    }

    stage('Deploy') {
      steps {
        sh 'java -jar -Dserver.port=8083 /home/ubuntu/petclinic-deploy/spring-petclinic-2.7.0-SNAPSHOT.jar'
      }
    }

  }
  tools {
    maven 'maven'
    jdk 'java11'
  }
}
