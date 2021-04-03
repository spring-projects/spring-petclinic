pipeline {
    agent any

    stages {
        stage('Checkout SCM') {
            steps {
                checkout scm
            }
        }
        stage ('Initialaze') {
            steps {
                sh '''
                    echo "PATH = ${PATH}"
                    echo "M2_HOME = ${M2_HOME}"
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
