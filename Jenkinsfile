pipeline{
    agent { label 'jdk-11-mvn' }
    parameters{
        choice(name: 'BRANCH_TO_BUILD', choices: ['google', 'amazon'], description: 'branches to be build')
        string(name: 'MAVEN_GOAL', defaultValue: 'package', description: 'maven goal')   
       }
    stages{
        stage('vcs'){
            steps{
               git branch: "${params.BRANCH_TO_BUILD}", url: 'https://github.com/vikasvarmadunna/spring-petclinic.git'
                 }
        }
     stage('build'){
            steps{
                agent { label 'jdk-11-mvn' }
                sh "/usr/share/maven/bin/mvn ${params.MAVEN_GOAL}"
                 }
        }
        stage('post') {
            steps {
            archiveArtifacts artifacts: 'target/spring-petclinic-*.jar'
            junit '**/surefire-reports/*.xml'
             }
     
        }
    }     
}
