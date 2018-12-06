pipeline {
    agent any
    triggers {
        pollSCM('H/5 * * * *')
    }
    tools {
        maven 'Maven3'
        jdk 'Java8'
    }
    stages {
        stage('Build') {
            steps {
                git url: 'https://github.com/Takayuki-sempai/spring-petclinic'
                bat 'mvn clean package'
                archiveArtifacts artifacts: '**/target/*.jar', fingerprint: true
            }
        }
        stage('SonarQube analysis') {
            steps {
                withSonarQubeEnv('sonar') {
                    bat 'mvn org.sonarsource.scanner.maven:sonar-maven-plugin:3.5.0.1254:sonar'
                }
            }
        }
    }
    post {
        always {
            emailext from: 'jenkins.test@inbox.ru', body: 'Build', subject: 'Build Failed', to: 'jenkinstest@rambler.ru'
        }
        failure {
            emailext from: 'jenkins.test@inbox.ru', body: 'Build failed', subject: 'Build Failed', to: 'jenkinstest@rambler.ru'
//        mail bcc: '', body: 'Build failed', cc: '', from: '', replyTo: '', subject: 'BuildFailed', to: 'jenkinstest@rambler.ru'
//        mail bcc: '', body: "<b>Example</b><br>Project: ${env.JOB_NAME} <br>Build Number: ${env.BUILD_NUMBER} <br> URL de build: ${env.BUILD_URL}", cc: '', charset: 'UTF-8', from: '', mimeType: 'text/html', replyTo: '', subject: "ERROR CI: Project name -> ${env.JOB_NAME}", to: 'jenkinstest@rambler.ru'
//        sh 'echo "This will run only if failed"'
        }
    }
}

