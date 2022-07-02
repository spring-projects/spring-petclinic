pipeline {
    agent none

    stages {
        stage('Build') {
	    agent {
	    	label 'oci'
	    }
            steps { //init
                echo 'initialize terraform'
		sh 'cd terraform'
		terraform init
            }
        }
        stage('Test') {
            steps {
                echo 'Testing..'
            }
        }
        stage('Deploy') {
            steps {
                echo 'Deploying....'
            }
        }
    }
}
