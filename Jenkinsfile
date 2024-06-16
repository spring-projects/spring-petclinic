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
        stage('OWASP Dependency-Check') {
            steps {
                dependencyCheck additionalArguments: '--scan target/', odcInstallation: 'OWASP Check'
            }
        }
        stage('Build & push Docker Image') {
            when {
                branch 'Release'
            }
            steps {
                script {
                    withDockerRegistry(credentialsId: 'c9b058e5-bfe6-41f8-9b5d-dc0b0d2955ac', toolName: 'docker') {
                        sh "docker build -t prasannakumarsinganamalla431/petclinic:${BUILD_NUMBER} -f .devcontainer/Dockerfile ."
                        sh "docker push prasannakumarsinganamalla431/petclinic:${BUILD_NUMBER}"

                    }
                }
            }
        }

    }   
}
