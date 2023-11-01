pipeline{
    agent{ label 'AGENT'}
    triggers{pollSCM ('* * * * *')}
    stages{
        stage('VCS'){
            steps{
                git url : 'https://github.com/Abhishek16tiwary/spring-petclinic.git',
                    branch : 'main'
            }
        }
        stage('Build'){
            steps{
                sh 'mvn package'
            }
        }
        stage('Sonar Cloud Analysis'){
            steps{
                withSonarQubeEnv('SONAR_CLOUD'){
                    sh 'mvn clean verify sonar:sonar -Dsonar.organization=springgit -Dsonar.projectKey=springgit'
                }
                
            }
        }
    }
}