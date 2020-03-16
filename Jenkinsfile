pipeline {
    agent any
    environment {
       branch = "${env.BRANCH_NAME}"
    }


    stages {
        stage('Build') {
            steps {
                sh 'mvn clean' 
            }
        }
	stage('Test') {
            steps {
                sh 'mvn test' 
            }
        }
	stage('Package') {
            steps {
                sh 'mvn package' 
            }
        }
   
	stage('Deploy') {
 	    when {
    	        expression {
                    return branch == 'master'
                }
            }
            steps {
                sh 'mvn deploy' 
            }
        }
    }
    
}
