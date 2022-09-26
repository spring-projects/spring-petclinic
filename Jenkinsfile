pipeline {
    agent any
    stages {
        stage('get') {
            steps {
                git branch: 'REL_INT_2.0', url: 'https://github.com/usorama/spring-petclinic.git'
            }
        }
        stage('build') {
            steps {
                sh 'mvn package'
            }
        }
        stage('Archive JUnit formatted test results') {
            steps {
                junit '**/surefire-reports/*.xml'
            }
        }
        stage('Archive artifacts') {
            steps {
            archive '**/target/*.jar'
            }
        }
    }    
}