pipeline{
    agent { label 'jdk-11-mvn' }
    parameters{
        choice(name: 'BRANCH_TO_BUILD', choices: ['google', 'amazon'], description: 'branches to be build')
        string(name: 'MAVEN_GOAL', defaultValue: 'package', description: 'maven goal')   
       }
    triggers { pollSCM('* * * * *') }
    stages{
        stage('vcs'){
            steps{
                mail subject: 'build started', body: 'build started', to: 'reachvikasvarma@gmail.com'
                git branch: "${params.BRANCH_TO_BUILD}", url: 'https://github.com/vikasvarmadunna/spring-petclinic.git'
                 }
        }
     stage('build'){
            steps{
                agent { label 'jdk-11-mvn' }
                sh "/usr/share/maven/bin/mvn ${params.MAVEN_GOAL}"
                 }
        }
        
    }
    
    post{

        always{
            echo 'job completed'
            mail subject: 'build failed', body: 'build failed', to: 'reachvikasvarma@gmail.com'
        }
        failure{
            mail subject: 'build failed', body: 'build failed', to: 'reachvikasvarma@gmail.com'
        }
        success{
            junit '**/surefire-reports/*.xml'    
        }
    }
}
