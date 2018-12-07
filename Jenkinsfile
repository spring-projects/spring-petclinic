pipeline {
    agent any
    triggers {
        pollSCM('H/5 * * * *')
    }
    tools {
        maven 'Maven3'
        jdk 'Java8'
    }
    currentBuild.displayName = '1.0.${BUILDS_THIS_YEAR}'
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
        failure {
            emailext from: 'jenkins.test@inbox.ru',
                    recipientProviders: [developers(), requestor()],
                    body: '$DEFAULT_CONTENT',
                    subject: '$DEFAULT_SUBJECT'
        }
    }
}

