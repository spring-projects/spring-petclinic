pipeline {
    agent any
    stages {
        stage('Build') {
            steps {
                sh './mvnw compile' 
            }
        }
        stage('Test') {
            steps {
                sh './mvnw test' 
            }
        }
        stage('Package') {
            steps {
                sh './mvnw package' 
            }
        }
        stage('Deploy') {
            steps {
                sh './mvnw deploy' 
            }
        }
    }
    post {
    	sucess {
        	echo $ echo "build passed" | mail -s "Jenkins Question 1 - Build Passed" bedardjake@gmail.com
    	}
   }

}
