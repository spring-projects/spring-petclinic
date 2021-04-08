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
                sh '''
                    mvn --version
                    mvn clean test serefire-report:report
                '''
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
