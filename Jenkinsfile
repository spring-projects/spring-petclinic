pipeline {
    agent any
    stages {
        stage('Build') {
            steps {
                sh './mvnw package' 
            }
        }
        stage('test') {
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
