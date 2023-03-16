pipeline {
    agent { label 'JDK_17' }
    triggers { pollSCM ('* * * * *') }
    parameters {
        choice(name: 'MAVEN_GOAL', choices: ['package', 'install', 'clean'], description: 'Maven Goal')
    }
    stages {
        stage('vcs') {
            steps {
                git url: 'https://github.com/Aseerwadham/spring-petclinic.git',
                    branch: 'main'
            }
        }
        stage('package') {
            tools {
                jdk 'JDK_17'
            }
            steps {
                sh "./mvnw ${params.MAVEN_GOAL}"
            }
        }
        stage('sonar analysis') {
            steps {
                withSonarQubeEnv('SONAR_CLOUD') {
                    sh './mvnw clean package sonar:sonar -Dsonar.organization=springpetclinic143 -Dsonar.projectkey=springpetclinic143_springpetclinic'
                }
            }
        }
        stage('post build') {
            steps {
                archiveArtifacts artifacts: '**/target/spring-petclinic-3.0.0-SNAPSHOT.jar',
                                 onlyIfSuccessful: true
                junit testResults: '**/surefire-reports/TEST-*.xml'
            }
        }
    }
}    
