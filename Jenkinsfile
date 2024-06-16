pipeline {
    agent any
    
    tools {
        jdk 'jdk17'
        maven 'maven3'
        
    }
    
    environment {
        SONAR_AUTH_TOKEN = credentials('sonarfile') // Assumes you have stored your token in Jenkins Credentials
    }
    
    stages {
        stage('compile ') {
            steps {
                sh 'mvn compile'
            }
        }
        stage('SonarQube Analysis') {
            steps {
                script {
                    // Run Maven with SonarQube plugin for analysis
                    withSonarQubeEnv('SonarQubeScanner') { // 'SonarQubeScanner' is the name of the SonarQube server configured in Jenkins
                        sh """
                            mvn sonar:sonar -Dsonar.login=${SONAR_AUTH_TOKEN} \
                            -Dsonar.projectName=spring-petclinic \
                            -Dsonar.java.binaries=. \
                            -Dsonar.projectKey=spring-petclinic
                        """
                        //mvn sonar:sonar sonar maven plugin command
                        //Command: $SCANNER_HOME/bin/sonar-scanner standalone sonarqub scanner  command
                    }
                }
            }
        }
        stage('Maven Package') {
            steps {
                sh 'mvn clean package -DskipTests'
            }
        }
        
    }   
}
