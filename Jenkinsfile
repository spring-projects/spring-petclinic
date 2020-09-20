 pipeline {
    agent none
    stages {
         stage('Back-end build') {
            agent {
                docker { 
                    image 'maven'
                    label 'master'  
                    args '-u root'
                 }
            }
            steps {
                sh 'mvn clean package'
                }
         }
         stage('Build petclinic') {
          agent { dockerfile true }
          steps {}
         }
    }
 }


