pipeline {
    agent any
    
    tools {
        jdk 'jdk17'
        maven 'maven3'
    }
    
    environment {
        SONAR_AUTH_TOKEN = credentials('SONAR_AUTH_TOKEN') // Assumes you have stored your token in Jenkins Credentials
        DOCKER_REGISTRY_CREDENTIALS = credentials('Docker_cred')

        
    }
    
    stages {
        stage('compile') {
            steps {
                sh 'mvn compile'
            }
        }
        
        stage('SonarQube Analysis') {
            when {
                branch 'Release'
            }
            steps {
                script {
                    // Run Maven with SonarQube plugin for analysis
                    withSonarQubeEnv('SonarQubeScanner') {
                        sh """
                            mvn sonar:sonar -Dsonar.login="${SONAR_AUTH_TOKEN}" \
                            -Dsonar.projectName=spring-petclinic \
                            -Dsonar.java.binaries=. \
                            -Dsonar.projectKey=spring-petclinic
                        """
                    }
                }
            }
        }
        
        stage("Quality Gate") {
            when {
                branch 'Release'
            }
            steps {
                timeout(time: 1, unit: 'HOURS') {
                    script {
                        def qg = waitForQualityGate()
                        if (qg.status != 'OK') {
                            error "Pipeline aborted due to quality gate failure: ${qg.status}"
                        }
                    }
                }
            }
        }
        stage('OWASP Dependency-Check') {
            when {
                branch 'Release'
            }
            steps {
                dependencyCheck additionalArguments: '--scan target/', odcInstallation: 'OWASP Check'
            }
        }
        
        stage('Maven Package') {
            steps {
                sh 'mvn clean package -DskipTests'
            }
        }
        stage('Building Docker Image') {
            
            steps {
                script {
                    withDockerRegistry(credentialsId: 'Docker_cred', toolName: 'Docker') {
                        sh "docker build -t prasannakumarsinganamalla431/petclinic:${BUILD_NUMBER} ."

                    }
                }
            }    
        }
        stage('pushing image to Docker hub') {
            
            steps {
                script {
                    withDockerRegistry(credentialsId: 'Docker_cred', toolName: 'Docker') {
                        sh "docker push prasannakumarsinganamalla431/petclinic:${BUILD_NUMBER}"
                        sh "docker run -d prasannakumarsinganamalla431/petclinic:${BUILD_NUMBER}"


                    }
                }
            }    
        }
    }   

}
