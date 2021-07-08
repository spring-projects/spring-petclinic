pipeline {
    agent { label 'MASTER' }           
    stages {
        stage('clone') {
            steps {
                git branch: 'wavefront', 
                url: 'https://github.com/kasasravankumar/spring-petclinic.git'
            }
        }
        stage('build'){
            steps{
                sh 'mvn package'
                
            }
        }
    }
}
