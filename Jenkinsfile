pipeline {
    agent {
    	label 'oci'
    }
    stages {
        stage('Build') {
            steps { //
                echo 'start build by mvn'
		        sh './mvnw package'
                sh 'ls target/*.jar'
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
