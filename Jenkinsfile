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
    post{
        success{
            mail subject : 'Jenkins job of ${JOB_NAME} of build numer ${BUILD_ID} is passed',
                 body : 'Click Here to see more details ${BUILD_URL}',
                 from : 'jenkins@outlook.com',
                 to : 'abhishek16tiwary@gmail.com'
        }
        failure{
            mail subject : 'Jenkins job of ${JOB_NAME} of build numer ${BUILD_ID} is Failed',
                 body : 'Click Here to see more details ${BUILD_URL}',
                 from : 'jenkins@outlook.com',
                 to : 'abhishek16tiwary@gmail.com'
        }
    }
}