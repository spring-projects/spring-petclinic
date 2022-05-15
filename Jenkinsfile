pipeline {
    agent { label 'JDK11' }
    options { 
        timeout(time: 1, unit: 'HOURS')
        retry(2) 
    }
    triggers {
        cron('0 * * * *')
    }
    stages {
        stage('Source Code') {
            git url: 'https://github.com/GitPracticeRepo/spring-petclinic.git', 
                branch: 'main'
        }
        stage('Build the Code') {
            sh script: 'mvn clean package'
        }
        stage('reporting') {
            junit testResults: 'target/surefire-reports/*.xml'
        }
    }
    post {
        success {
            // send the success email
            echo "Success"
        }
        unsuccessful {
            //send the unsuccess email
            echo "failure"
        }
    }
}