pipeline{
agent any
stages {
     stage('scm') {
        steps {
               git url: "https://github.com/nagarjuna33/spring-petclinicnew.git", 
                     branch:"main"
               }
     }

        stage ('SONAR QUBE SCAN') {
            steps{
                sh 'mvn clean install'
}
}
}
}
    
