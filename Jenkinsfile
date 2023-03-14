pipeline {
    agent { label 'UBUNTU_NODE1' }
    triggers { pollSCM('* * * * *') }
    stages {
        stage('get url') {
            steps {
                git branch: 'sweety',
                    url: 'https://github.com/Madhuri-chinta/spring-petclinic.git'     
            }
        }    
        stage('package') {
            steps {
                sh './mvnw package'
            } 
        } 
        stage('sonarqube') {
            steps {
                withSonarQubeEnv('madhuri') {
                    sh 'mvn clean package sonar:sonar'
              }
            }
        }   
        }          
    }    

    

    
