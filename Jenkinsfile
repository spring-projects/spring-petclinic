pipeline{
    agent{ label 'AGENT'}
    triggers{pollSCM ('* * * * *')}
    stages{
        stage('VCS'){
            steps{
                git url : 'https://github.com/Abhishek16tiwary/spring-petclinic.git',
                branch : main
            }
        }
        stage('Build'){
            steps{
                sh 'mvn package'
            }
        }
    }
}