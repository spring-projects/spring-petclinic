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
            steps {
                sh './mvnw deploy'
            }
        }
    }
    post {
        always {
            echo 'One way or another, I have finished'
     /*       deleteDir() /* clean up our workspace */
        }
        success {
            echo 'I succeeeded!'
        }
        unstable {
            echo 'I am unstable :/'
        }
        failure {
            echo 'I failed :('
        }
        changed {
            echo 'Things were different before...'
        }
    }
}
