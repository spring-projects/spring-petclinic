pipeline {

  agent {
    kubernetes {
      label 'jenkins-jenkins-agent'
      defaultContainer 'docker-builder'
    }
  }

  parameters {
    string(name: 'BRANCH', defaultValue: 'main', description: 'Branch name')
  }

  environment {
    DOCKERHUB_TOKEN       = credentials('dockerhub-full')
  }

  stages {

    stage('Checkout cloud') {
      steps {
        git branch: """${params.BRANCH}""",
        credentialsId: 'github_key',
        url: "git@github.com:mariiayakubenko/spring-petclinic.git"
      }
    }

    stage('Build') {

      steps {
        sh """
           ./mvnw package -t mariiayakubenko/petclinic:${BUILD_ID} -t mariiayakubenko/petclinic:latest
	   """
      }
    }

    stage('Create artifact') {

      steps {
        sh """
           docker login -u mariiayakubenko --password $DOCKERHUB_TOKEN
	         docker push -a mariiayakubenko/petclinic
           """
      }
    }
  }
}
