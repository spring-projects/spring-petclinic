pipeline {
    agent any
    stages {
        stage('Build') {
            steps {
                sh './mvnw clean' 
            }
        }
        stage('Test') {
            steps {
                sh './mvnw test' 
            }
        }
        stage('Package') {
            steps {
                sh './mvnw package' 
            }
        }
        stage('Deploy') {
            when {
                branch 'master'
            }
            steps {
                sh './mvnw deploy' 
            }
        }
    }
    post {
        success {
            mail to: 'bit172@gmail.com',
                subject: "Successful Pipeline: ${currentBuild.fullDisplayName}",
                body: "Successful build ${env.BUILD_URL}"
        }
        failure {
            mail to: 'bit172@gmail.com',
                subject: "Failed Pipeline: ${currentBuild.fullDisplayName}",
                body: "Build Failure, location to build ${env.BUILD_URL}"
        }

    }
}
