pipeline{
    agent any
    stages{
        stage('vcs'){
            steps{
               git url: 'https://github.com/vikasvarmadunna/spring-petclinic.git', branch: 'google'
                 }
        }
    
        
        stage('build'){
            steps{
                agent { label 'jdk-11-mvn' }
                sh 'mvn package'
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
