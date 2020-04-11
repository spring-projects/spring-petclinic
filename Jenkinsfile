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
                echo "Branch name: ${env.BRANCH_NAME}"
                sh './mvnw deploy'
            }
        }
    }
    post {
        always {
            echo 'One way or another, I have finished'
        }
        success {
            echo 'I succeeeded!'
            // For GitHub
sh 'curl "https://api.GitHub.com/repos/daphneaugier/spring-petclinic/statuses/$GIT_COMMIT?access_token=5cff02b111407f3c0bd4e30c8fb215c1369c46e3" \
  -H "Content-Type: application/json" \
  -X POST \
  -d "{\"state\": \"success\",\"context\": \"continuous-integration/jenkins\", \"description\": \"Jenkins\", \"target_url\": \"http://localhost:9090/job/spring-petclinic/$BUILD_NUMBER/console\"}"'
            // Email
             mail to: 'daphne.augier@gmail.com',
             subject: "Successful Pipeline: ${currentBuild.fullDisplayName}",
             body: "Eveythink OK with ${env.BUILD_URL}\n"
        }
        unstable {
            echo 'I am unstable :/'
        }
        failure {
            echo 'I failed :('
             // For GitHub
sh 'curl "https://api.GitHub.com/repos/daphneaugier/spring-petclinic/statuses/$GIT_COMMIT?access_token=5cff02b111407f3c0bd4e30c8fb215c1369c46e3" \
  -H "Content-Type: application/json" \
  -X POST \
  -d "{\"state\": \"failure\",\"context\": \"continuous-integration/jenkins\", \"description\": \"Jenkins\", \"target_url\": \"http://localhost:9090/job/spring-petclinic/$BUILD_NUMBER/console\"}"'
       }
        changed {
            echo 'Things were different before...'
        }
    }
}