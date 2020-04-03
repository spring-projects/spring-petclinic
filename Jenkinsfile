pipeline {
    agent {
        docker {
			image 'maven:alpine' 
            args '-v /root/.m2:/root/.m2' 
        }
    }
    stages {
        stage('Build') { 
            steps {
                sh 'mvn -B -DskipTests clean install'
            }
        }
    }
}