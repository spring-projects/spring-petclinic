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
            echo "Branch name: $(env.BRANCH_NAME)"
            when {
                  branch 'master'
            }
            steps {
                echo "Merge to master"
                sh 'git pull'
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
curl "https://api.GitHub.com/repos/daphneaugier/spring-petclinic/statuses/$GIT_COMMIT?access_token=<YOUR_GITHUB_TOKEN>" \
  -H "Content-Type: application/json" \
  -X POST \
  -d "{\"state\": \"success\",\"context\": \"continuous-integration/jenkins\", \"description\": \"Jenkins\", \"target_url\": \"http://localhost:9090/job/spring-petclinic/$BUILD_NUMBER/console\"}"
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
        }
        changed {
            echo 'Things were different before...'
        }
    }
}