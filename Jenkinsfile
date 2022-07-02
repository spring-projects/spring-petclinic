pipeline {
    agent {
    	label 'oci'
    }
    stages {
        stage('Build') {
            steps { //
                echo 'start build by mvn'
		        sh '/usr/bin/terraform version'
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
