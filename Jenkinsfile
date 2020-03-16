pipeline {
    agent any
    environment {
       branch = "${env.BRANCH_NAME}"
    }


    stages {
        stage('Build') {
            steps {
    		slackSend color: 'good', message: 'Build step'
                sh 'mvn clean' 
            }
        }
	stage('Test') {
            steps {
		slackSend color: 'good', message: 'Test step'
                sh 'mvn test' 
            }
        }
	stage('Package') {
            steps {
		slackSend color: 'good', message: 'Package step'
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
		slackSend color: 'good', message: 'Deployment step'
                sh 'mvn deploy' 
            }
        }
    }
    
    slackSend color: 'good', message: 'Build Succeeded'
}
