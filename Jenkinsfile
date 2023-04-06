pipeline{
agent { label 'node' }
// triggers { pollSCM ('H * * * 1-5') }
parameters {
    choice (name: 'BRANCH_TO_BUILD', choices: ['main'], description: 'Branch to build')
    string (name: 'MAVEN_GOAL', defaultValue: 'clean install', description: 'maven goal')
}
stages {
     stage('scm') {
        steps {
               git url: "https://github.com/nagarjuna33/spring-petclinicnew.git", 
                     branch:"${params.BRANCH_TO_BUILD}"
               }
     }

        stage ('sonarqube') {
            steps{
                withSonarQubeEnv('sonarqube') {
                    sh 'mvn clean package sonar:sonar'
            }
            }
        }
    }
}


