pipeline {
    agent any
    tools {
        maven 'maven-3.5.0'
    }
    stages {
        stage ('Build') {
            steps {
                git 'https://github.com/liatrio/spring-petclinic'
                sh 'mvn clean install'
            }
        }
    }
}
