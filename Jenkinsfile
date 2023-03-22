pipeline {
    agent any

    tools {
        maven "mvn-3.9.1"
        jdk "jdk-temurin-17"
    }

    stages {
        /*
        stage('Checkout Git') {
            steps {
                git branch: 'main', url: 'https://github.com/sukrucakmak/spring-petclinic.git'
            }
        */
        stage('Build') {
            steps {
                echo "Java Home: $env.JAVA_HOME"
                sh "mvn -Dmaven.test.failure.ignore=true clean package"
            }
        }
        stage('Quality Analysis') {
            steps {
                withSonarQubeEnv('sonar') {
                    sh 'mvn sonar:sonar'
                }
            }
        }
        stage('Quality Gate Control') {
            steps {
                timeout(time: 1, unit: 'HOURS') {
                    waitForQualityGate abortPipeline: true
                }
            }
        }
        stage('Deploy to Test') {
            steps {
                echo "Java Home: $env.JAVA_HOME"
            }
        }
    }
}
