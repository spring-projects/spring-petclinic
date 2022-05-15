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
            steps {
                git url: 'https://github.com/GitPracticeRepo/spring-petclinic.git', 
                branch: 'main'
            }
            
        }
        stage('Build the Code') {
            steps {
                sh script: 'mvn clean package'
            }
        }
        stage('reporting') {
            steps {
                junit testResults: 'target/surefire-reports/*.xml'
            }
            
        }
    }
    post {
        success {
            // send the success email
            echo "Success"
            mail bcc: '', body: 'Build Success', cc: '', from: 'devops@qtdevops.com', replyTo: '', subject: 'Build Succeded', to: 'qtdevops@gmail.com'
        }
        unsuccessful {
            //send the unsuccess email
            echo "failure"
        }
    }
}