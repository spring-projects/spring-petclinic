pipeline {
  agent {
    kubernetes {
      label 'promo-app'  // all your pods will be named with this prefix, followed by a unique id
      idleMinutes 5  // how long the pod will live after no jobs have run on it
      yamlFile 'build-pod.yaml'  // path to the pod definition relative to the root of our project 
      defaultContainer 'maven'  // define a default container if more than a few stages use it, will default to jnlp container
    }
  }
  stages {
    stage('scm'){
      steps {
        git 'https://github.com/spring-projects/spring-petclinic.git'
    }
    }
    stage('Build'){
      steps {
        sh  'mvn clean package -DskipTests=true'
    }
    }
}
}
