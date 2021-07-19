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
                withMaven {
                    sh "mvn clean compile"
                }
            }
        }
    }

}


