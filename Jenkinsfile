pipeline{
  agent any

  environment{
    REGISTRY_URL = "https://hub.docker.com/repositories/prankumar313"
    IMAGE_NAME = "spring-petclinic"
    GIT_COMMIT_SHORT = "${env.GIT_COMMIT[0..6]}"
  }
  
  stages{
    stage('Checkstyle'){
      when{
        not{
          branch 'main'
        }
      }
      steps{
        sh './gradlew checkstyleMain'
        archiveArtifacts artifacts: '**/build/reports/checkstyle/*.xml', allowEmptyArchive: true
      }
    }

    stage('Test'){
      when{
        not{
          branch 'main'
        }
      }
      steps{
        sh './gradlew test'
        junit '**/build/test-results/test/*.xml'
      }
    }

    stage('Build'){
      steps{
        sh './gradlew clean build -x test'
      }
    }

    stage('Build Docker Image'){
      steps{
        script{
          def ImageTag = env.BRANCH_NAME == 'main' ? 'latest' : GIT_COMMIT_SHORT
	  sh "docker build -t $REGISTRY_URL/${IMAGE_NAME}:${imageTag} ."
	}
      }
    }
    
    stage('Push Docker Image'){
      steps{
        script{
          def targetRepo = env.BRANCH_NAME == 'main' ? 'main' : 'mr'
          def imageTag = env.BRANCH_NAME == 'main' ? 'latest' : GIT_COMMIT_SHORT
          sh """
            echo $DOCKER_PASSWORD | docker login -u $DOCKER_USERNAME --password-stdin $REGISTRY_URL
            docker tag $REGISTRY_URL/$(IMAGE_NAME):${imageTag} $REGISTRY_URL/${targetRepo}/${IMAGE_NAME}:${imageTag}
            docker push $REGISTRY_URL/${targetRepo}/${IMAGE_NAME}:${imageTag}
          """
        }
      }
    }
  }
}

