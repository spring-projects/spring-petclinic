pipeline {
    agent any
    stages {
        stage('Build') {
            steps {
                sh './mvnw package' 
            }
        }
        stage('Test') {
            steps {
                sh './mvnw package'
            }
        }
        stage('Package') {
            steps {
                sh './mvnw package'
            }
        }
        stage('Deploy') {
            steps {
                sh './mvnw package'
            }
        }
    } post {
    failure {
        mail to: 'firassawan@icloud.com',
             subject: "Failed Pipeline: ${currentBuild.fullDisplayName}",
             body: "Something is wrong with ${env.BUILD_URL}"
         }
    }
}
