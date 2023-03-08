pipeline {
    agent { label 'MAVEN_JDK8' }
    triggers { pollSCM ('* * * * *') }
    parameters {
        choice(name: 'MAVEN_GOAL', choices: ['package', 'install', 'clean'], description: 'Maven Goal')
    }
    stages {
        stage('vcs') {
            steps {
                git url: 'https://github.com/Nanditha54/spring-petclinic.git',
                    branch: 'declarative'
            }
        }
        stage('package') {
            tools {
                jdk 'JDK_17'
            }
            steps {
                sh 'mvn package'
                sh "mvn ${params.MAVEN_GOAL}"
            }
        }
        stage('sonar analysis') {
            steps {
                // performing sonarqube analysis with "withSonarQubeENV(<Name of Server configured in Jenkins>)"
                withSonarQubeEnv('SONAR_CLOUD'),
                    sh 'mvn verify org.sonarsource.scanner.maven:sonar-maven-plugin:sonar -Dsonar.projectKey=nan_sonarcube'
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