pipeline {
    agent any

    stages {
        stage('Checkout SCM') {
            steps {
                checkout scm
            }
        }
        stage ('Test maven') {
            steps {
                try {
                sh '''
                    mvn --version
                    mvn clean test serefire-report:report
                '''
                } catch(err) {
                    sh "echo error Maven test"
                }
            }
        }
        stage('Build') {
            steps {
                echo 'Running build automation'
                sh '''
                        ./mvnw package
                   '''
           }
        }
        stage('CREATE ARTIFACT') {
            steps {
                echo 'Creating artifact..'
            }
        }
        stage('Deploy') {
            steps {
                echo 'Deploying....'
            }
        }
    }
}
