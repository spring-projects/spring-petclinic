#!/usr/bin/env groovy
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
                  branch 'development'
            }
            steps {
                echo "DEPLOY!"
             //   sh './mvnw deploy'
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
             mail to: 'm.augier@me.com',
             subject: "Successful Pipeline: ${currentBuild.fullDisplayName}",
             body: "Eveythink OK with ${env.BUILD_URL}\n"
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
