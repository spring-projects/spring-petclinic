pipeline {
    agent  { label 'jdk-11-mvn' }
    parameters {
        choice(name: 'BRANCH_TO_BUILD', choices: ['google', 'main'], description: 'Branch to build')
        string(name: 'MAVEN_GOAL', defaultValue: 'package', description: 'maven goal')
    }
    triggers {
        
        pollSCM('* * * * *')
    }
    stages {
        stage('vcs') {
            steps {
                mail subject: 'Build Started', 
                  body: 'Build Started', 
                  to: 'reachvikasvarma@gmail.com' 
                git branch: "${params.BRANCH_TO_BUILD}", url: 'https://github.com/vikasvarmadunna/spring-petclinic.git'
            }


  
        }
        stage('build') {
            steps {
                sh "/usr/share/maven/bin/mvn ${params.MAVEN_GOAL}"
            }
        }

    }
    post {
        always {
            echo 'Job completed'
            mail subject: 'Build Completed', 
                  body: 'Build Completed', 
                  to: 'reachvikasvarma@gmail.com'
        }
        failure {
            mail subject: 'Build Failed', 
                  body: 'Build Failed', 
                  to: 'reachvikasvarma@gmail.com' 
        }
        success {
            junit '**/surefire-reports/*.xml'
        }
    }


    
          
            
    

          
    
    
  
}
