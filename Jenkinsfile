pipeline {
    agent {
    	label 'oci'
    }
    stages {
        stage('Build') {
            steps { //
                echo 'start build by mvn'
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
