pipeline {
    agent any
    stages {
        stage('Build') {
            steps {
               bat  './mvnw compile'
            }
        }
         stage('Test') {
            steps {
                bat './mvnw test'
            }
        }
        stage('Package') {
            steps {
                bat './mvnw package'
            }
        }
        stage('Deploy') {
            steps {
                echo 'Deployed Successfully'
            }
        }  
    }
    post {
    success {
        mail (
            to: "mnezam.31@gmail.com",
            subject: "Successful Build ${currentBuild.fullDisplayName}",
            body: "Build successful with the following ${env.BUILD_URL}"
            )
    }
    failure {
        mail (
            to: "mnezam.31@gmail.com",
            subject: "Build Failure ${currentBuild.fullDisplayName}",
            body: "Build Failure has occured with the following ${env.BUILD_URL}"
            )
    }
}
}
