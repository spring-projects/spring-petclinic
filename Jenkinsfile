pipeline {
    agent any
    tools {
        jdk 'JDK 8'
    }
    environment {
        SPRING_PROFILES_ACTIVE = "ci"
    }
    stages {
        stage('Initialize') {
            steps {
                sh "printenv"
            }
        }
        stage('Compile') {
            steps {
                withMaven(maven: 'M3', options: [artifactsPublisher(disabled: true), jacocoPublisher(disabled: true)]) {
                    sh "mvn clean compile"
                }
            }
        }
    }

}


