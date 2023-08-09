pipeline {
    agent any
    options {
        timeout(time: 30, unit: 'MINUTES')
    }
    triggers {
        pollSCM('* * * * *')
    }
    tools {
        jdk 'JDK_17'
        maven 'MAVEN_HOME'
    }
    stages {
        stage ('vcs') {
            steps {
                git url: 'https://github.com/spring-projects/spring-petclinic.git',
                branch: 'main'   
            }
        }
        stage ('SonarQube analysis') {
            steps {
                // performing sonarqube analysis with "withSonarQubeENV(<Name of Server configured in Jenkins>)"
                withSonarQubeEnv ('SONAR_CLOUD') {
                // requires SonarQube Scanner for Maven 3.2+
                    sh 'mvn clean package sonar:sonar \
                       -Dsonar.organization=vinodreddy \
                       -Dsonar.token=8c25ce4838b681386906760fda5459c4f80eedf6 \
                       -Dsonar.projectKey=vinodreddy_spc'
                }
            }
        }
        stage('build') {
            steps {
                junit testResults: '**/target/surefire-reports/TEST-*.xml'
            }
        }
    }
}
    
        