pipeline {
  agent any

  stages {

    stage('Checkout') {
      steps {
        git 'https://github.com/Guru911/spring-petclinic.git'
      }
    }

    stage('Build') {
      steps {
        sh 'mvn clean package -DskipTests'
      }
    }

    stage('Test') {
      steps {
        sh 'mvn test'
      }
    }

    stage('SonarQube Analysis') {
      steps {
        withSonarQubeEnv('sonarqube') {
          sh 'mvn sonar:sonar'
        }
      }
    }

    stage('Docker Build') {
      steps {
        sh 'docker build -t petclinic:1.0 .'
      }
    }

    stage('Run Application') {
      steps {
        sh '''
          docker rm -f petclinic || true
          docker run -d -p 8081:8080 --name petclinic petclinic:1.0
        '''
      }
    }
  }
}
