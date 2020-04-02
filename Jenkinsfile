pipeline {
    agent any
    stages {
        stage('Build') {
            steps {
                sh 'mvn compile' 
            }
        }
        stage('Test') {
            steps {
                sh 'mvn clean test' 
            }
        }
        stage('Package') {
            steps {
                sh './mvnw package' 
            }
        }
        stage('Deploy') {
            steps {
                echo 'Deploying'
            }
        }
    }
    post {
        always {
            emailext body: 'Jenkins Build Passed Hurray!', recipientProviders: [[$class: 'DevelopersRecipientProvider'], [$class: 'RequesterRecipientProvider']], subject: 'Jenkins Build [Spring-Petclinic]'
        }
    }
}