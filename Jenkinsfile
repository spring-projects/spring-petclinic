pipeline {
  agent {label "slave"}
stages {
  stage("build") {
    steps {
      sh "pwd"
      sh "ls -lrtha"
      dir("spring-petclinic") {
      sh "./mvnw package"
      }
    }
  }
  stage("deploy") {
    steps {
      dir("spring-petclinic") {
    sh """
    docker build -t pet-clinic:1.0 .
    docker rm pet-clinic_container
    docker run --name pet-clinic_container -d -p 8080:8080 pet-clinic:1.0
    """
      }
    }
  }
}
}
