pipeline {
    agent { label 'worker1' }
    stages {
        stage('Docker Build') {
            steps {
      	        sh 'docker build -t spring-petclinic:v1 .'
            }
        }
    }
    

}
