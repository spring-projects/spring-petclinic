pipeline {
    agent any

    tools {
        maven 'Maven'
    }

    stages {

        stage('Clone Repository') {
            steps {
                git 'https://github.com/PipelineNinja/spring-petclinic.git'
            }
        }

        stage('Build Application') {
            steps {
                sh 'mvn clean package'
            }
        }

        stage('Verify Build') {
            steps {
                sh 'ls target'
            }
        }

    }
}
