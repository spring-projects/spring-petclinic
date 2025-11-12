pipeline {
    agent any
    
    tools {
        maven 'Maven 3.9.5'
        jdk 'JDK 25'
    }
    
    triggers {
        pollSCM('H/5 * * * *')
    }
    
    environment {
        MAVEN_OPTS = '-Xmx1024m'
        PROJECT_NAME = 'spring-petclinic'
        SONAR_PROJECT_KEY = 'spring-petclinic'
    }
    
    stages {
        stage('Checkout') {
            steps {
                echo 'Checking out code...'
                checkout scm
                script {
                    sh 'git rev-parse --short HEAD'
                    sh 'git log -1 --pretty=%B'
                }
            }
        }
        
        stage('Build') {
            steps {
                echo 'Building project...'
                sh './mvnw clean compile -DskipTests'
            }
        }
        
        stage('Test') {
            steps {
                echo 'Running tests...'
                sh './mvnw test'
            }
            post {
                always {
                    junit testResults: '**/target/surefire-reports/*.xml', allowEmptyResults: true
                    jacoco(
                        execPattern: '**/target/jacoco.exec',
                        classPattern: '**/target/classes',
                        sourcePattern: '**/src/main/java',
                        exclusionPattern: '**/*Test*.class'
                    )
                }
            }
        }
        
        stage('SonarQube Analysis') {
            steps {
                echo 'Running SonarQube analysis...'
                withSonarQubeEnv('SonarQubeServer') {
                    sh """
                        ./mvnw clean verify sonar:sonar \
                          -Dsonar.projectKey=${env.SONAR_PROJECT_KEY} \
                          -Dsonar.projectName=${env.PROJECT_NAME} \
                          -Dsonar.projectVersion=${env.BUILD_NUMBER} 
                    """
                }
            }
        }
        
        stage('Quality Gate') {
            steps {
                timeout(time: 15, unit: 'MINUTES') {
                    waitForQualityGate abortPipeline: true
                }
            }
        }
        
        stage('Code Quality') {
            steps {
                echo 'Running checkstyle...'
                sh './mvnw checkstyle:checkstyle'
            }
            post {
                always {
                    recordIssues(
                        enabledForFailure: true,
                        tool: checkStyle(pattern: '**/target/checkstyle-result.xml')
                    )
                }
            }
        }
        
        stage('Package') {
            steps {
                echo 'Packaging application...'
                sh './mvnw package -DskipTests'
            }
        }
        
        stage('Archive') {
            steps {
                echo 'Archiving artifacts...'
                archiveArtifacts artifacts: '**/target/*.jar', 
                                fingerprint: true,
                                allowEmptyArchive: false
            }
        }
    }
    
    post {
        success {
            echo 'Build successful!'
        }
        failure {
            echo 'Build failed!'
        }
        always {
            cleanWs(
                cleanWhenNotBuilt: false,
                deleteDirs: true,
                disableDeferredWipeout: true,
                notFailBuild: true
            )
        }
    }
}

