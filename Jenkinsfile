pipeline {
    agent {
    	label 'oci'
    }
    stages {
        stage('Build') {
            steps { //init
                echo 'initialize terraform'
		        sh 'env'
                sh 'docker version'
		        //sh 'cd terraform'
         		//sh 'terraform init'
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
