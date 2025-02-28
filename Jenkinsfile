pipeline {
  agent none

  stages {
    stage('Checkstyle') {
      agent {
        docker { image 'maven:3.8.5-openjdk-17' }
       }
      steps {
        checkout scm
        sh 'mvn checkstyle:checkstyle'
      }
      post {
        always {
          archiveArtifacts artifacts: 'target/checkstyle-result.xml', allowEmptyArchive: true
        }
      }
    }
    stage('Test') {
      agent {
        docker { image 'maven:3.8.5-openjdk-17' }
       }
      steps {
        sh 'mvn test -Dcheckstyle.skip=true'
      }
    }
    stage('Build') {
      agent {
        docker { image 'maven:3.8.5-openjdk-17' }
      }
      steps {
        sh 'mvn clean package -DskipTests -Dcheckstyle.skip=true'
      }
      post {
        always {
          archiveArtifacts artifacts: 'target/*.jar', allowEmptyArchive: true
        }
      }
    }
    stage('Build Image') {
      agent {
        docker {
          image 'docker:20.10.16'
          args '--privileged -u root -v /var/run/docker.sock:/var/run/docker.sock'
        }
      }
      steps {
        script {
          def docker_image=docker.build("mr:8084/spring-petclinic:$GIT_COMMIT")
          sh 'docker login -u "$REGISTRY_USER" -p "$REGISTRY_PASS" mr:8084'
          docker.withRegistry('mr:8084') {
            docker_image.push()
          }
        }
      }
    }
  }
}
