pipeline {
    agent{label "jdk8"}
    stages{
        stage ("pull from vcs" ){ 
        
           steps{
            git url:https://github.com/Qtalha/spring-petclinic.git
            branch: "rel-race" 
           }
        
        }
        stage("build"){
            steps{
                sh 'mvn pacakge'
            }
        }
    }
}
