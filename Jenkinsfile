pipeline {
    agent none

    stages {
        stage('Build') {
	    agent {
	    	label 'oci'
	    }
            steps {
                echo 'Building..'
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
