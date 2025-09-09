pipeline {
    agent any
    stages{
        stage('checkout')
        {
            steps{
                echo "Clonning Reposotiory"
                checkout scm
            }
        }
        stage('Cleaning stage'){
            steps{
                echo "Builing jar file without testing"
                sh'./mvnw clean'
            }
        }
        stage('build stage'){
            steps{
                echo "Builing jar file without testing"
                sh'./mvnw package -DskipTests'
            }
        }
        stage('test stage'){
            steps{
                echo "Running Unit tests"
                withSonarQubeEnv('SonarQube-server') {
                    sh'./mvnw sonar:sonar'
                }
            }
        }
        stage('dockerizing stage'){
            sh'docker '
        }
    }
}