pipeline {
    agent any

    triggers {
        cron('H/3 * * * 4') // Runs every 3 minutes on Thursdays
    }

    tools {
        maven 'Maven 3'
        jdk 'JDK 11'
    }

    stages {
        stage('Checkout') {
            steps {
                git 'https://github.com/girisujan2001/spring-petclinic.git'
            }
        }

        stage('Build') {
            steps {
                sh 'mvn clean package'
            }
        }

        stage('Test & Code Coverage') {
            steps {
                sh 'mvn test'
                sh 'mvn jacoco:report'
            }
        }

        stage('Archive Artifacts') {
            steps {
                archiveArtifacts artifacts: 'target/*.jar', fingerprint: true
            }
        }

        stage('Publish Jacoco Report') {
            steps {
                jacoco execPattern: '**/target/jacoco.exec', classPattern: '**/target/classes', sourcePattern: '**/src/main/java'
            }
        }
    }

    post {
        success {
            echo 'Pipeline executed successfully!'
        }
        failure {
            echo 'Pipeline failed!'
        }
    }
}

