pipeline {
    agent {
      docker {
        image 'maven:3-alpine' 
        args '-v /root/.m2:/root/.m2' 
      }        
    }

    stages {
        stage('Checkout SCM') {
            steps {
                checkout scm
            }
        }
        stage ('Initialize') {
            steps {
                sh '''
                    echo "PATH = ${PATH}"
                    echo "M2_HOME = ${M2_HOME}"
                ''' 
            }
        }
        stage('Build') {
            steps {
                sh 'chmod +x ./gradlew'
                sh './gradlew build -x test'
           }
        }
        stage('Test') {
            steps {
                echo 'Testing..'
            }
        }
        stage('Deploy') {
            steps {
                echo 'Deploying....'
            }
        }
    }
}