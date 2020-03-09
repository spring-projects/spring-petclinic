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
    }
    post {
        success {
        mail to: 'firassawan@icloud.com',
             subject: "Succeeded Pipeline: ${currentBuild.fullDisplayName}",
             body: "Congrats ${env.BUILD_URL}"
    }

    }
}
