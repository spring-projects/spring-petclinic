pipeline {
    agent any
    options {
        timeout(time: 30, unit: 'MINUTES') 
    }
    triggers {
        pollSCM('* * * * *')
    }
    tools {
        maven 'Maven_3.8'
        jdk 'JDK_17' 
    }
    stages {
        stage ('git'){
            steps{
            git branch: 'develop',
                url: 'https://github.com/Akhil-Tejas225/spring-petclinic.git'
            }
        }
        stage('build and package') {
           steps { 
            withSonarQubeEnv('SONAR_CLOUD') {
                sh 'mvn clean package:sonar -Dsonar.organization=Akhil-Tejas225 -Dsonar.token=65fa1d49335531273f064cf5bd21d6c43c30bbc9 -Dsonar.projectKey=spring-petclinic'

            }
           }
           }
        stage('reporting'){
            steps {
            junit testResults: '**/target/surefire-reports/TEST-*.xml'
        }
        }


        
        }
    }
  