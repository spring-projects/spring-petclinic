// Pipeline Code for SpringPetClinic using Java11 and Maven
// Exercise part of Jenkins Declarative Pipeline Learning
pipeline {
    agent {label '2ND-NODE'}
    parameters {
        choice(name: 'BRANCH_TO_BUILD', choices: ['REL_INT_3.0', 'main'], description: 'Branch to build')
        string(name: 'MAVEN_GOAL', defaultValue: 'package', description: 'Maven Goal')
    }
//  Section defining different stages of build and actions if any    
    stages {
        stage('get code') {
            steps {
//              Adding email jobs with triggers
                mail subject: "Build Started for Jenkins JOB $env.JOB_NAME",
                        body: "Building $env.JOB_NAME",
                        to: 'qtuudhya@gmail.com'
                git branch: "${params.BRANCH_TO_BUILD}", url: 'https://github.com/usorama/spring-petclinic.git'
            }
        }
        stage('build') {
            steps {
                sh "mvn ${params.MAVEN_GOAL}"
            }
        }
        stage('Archive test results') {
            steps {
                junit '**/surefire-reports/*.xml'
            }
        }
        stage('Archive artifacts') {
            steps {
            archive '**/target/*.jar'
            }
        }
    }    
//  POST Build Actions Section    
    post {
        always {
//          echo "Job $env.JOB_NAME completed"
            mail subject: "Build Completed $env.JOB_NAME",
                    body: "Build Completed for $env.JOB_NAME \n Click here: $env.JOB_URL",
                    to: 'qtuudhya@gmail.com'                 
        }
        failure {
            mail subject: "Build Failed $env.JOB_NAME",
                    body: "Build Failed for $env.JOB_NAME \n Click here: $env.JOB_URL",
                    to: 'qtuudhya@gmail.com'            
        }
        success {
            mail subject: "Build Completed Successfully for $env.JOB_NAME",
                    body: "Build Completed Successfully for $env.JOB_NAME \n Click here: $env.JOB_URL",
                    to: 'qtuudhya@gmail.com'
            junit '**/surefire-reports/*.xml'        
        }

    }        
}
