pipeline {
    agent any
    options {
        skipStagesAfterUnstable()
    }
    stages {
        stage('Build') {
            steps {
                echo 'Building'
                sh './mvnw package' 
            }
        }
        stage('Test') {
            steps {
                echo 'Testing'
            }
        }
        stage('Package') {
            steps {
                echo 'Package'
            }
        }
        stage('Deploy') {
            steps {
                echo 'Deploying'
            }
        }
    }
}
