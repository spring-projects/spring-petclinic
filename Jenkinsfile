pipeline {
  agent any
stages {
  stage("build") {
    steps {
      sh "pwd"
      sh "ls -lrtha"
      sh "cd spring-petclinic"
      sh "./mvnw package"
    }
  }
  stage("deploy") {
    sh "cd spring-petclinic"
    sh """
    docker build -t pet-clinic:1.0 .
    docker run -d -p 8080:8080 pet-clinic:1.0
    """
  }
}
}
