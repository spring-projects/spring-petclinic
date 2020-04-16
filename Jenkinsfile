pipeline {
    agent any
    options {
        skipStagesAfterUnstable()
    }
    stages {
        stage('Build') {
            steps {
                echo 'Building'
                sh './mvnw compile' 
            }
        }
        stage('Test') {
            steps {
                echo 'Testing'
                sh './mvnw test'
            }
        }
        stage('Package') {
            steps {
                echo 'Package'
                sh './mvnw package'
            }
        }
        stage('Deploy') {
            steps {
                echo 'Deploying'
                sh './mvnw deploy'
            }
        }
    }
}
