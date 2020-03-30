pipeline {
  agent any
  stages {
    stage('Build') {
      steps {
        bat './mvnw package'
      }
    }
     stage('Test') { 
            steps {
                bat 'make check'
            }
        }
    stage('Package') { 
            steps {
                echo 'Testing'
            }
        }
    stage('Deploy') { 
        steps {
           echo 'Deploy'
        }
     }
  }
}
post {
    failure {
        mail to: 'jessica.allaire.96@hotmail.com',
             subject: "Failed Pipeline: ${currentBuild.fullDisplayName}",
             body: "Something is wrong with ${env.BUILD_URL}"
    }
}
