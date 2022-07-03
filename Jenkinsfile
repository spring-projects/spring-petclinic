pipeline {
    agent {
    	label 'oci'
    }
    stages {
        stage('Build') {
            steps {
                sh 'cd terraform'
                    script {
                        terraform_formation = true
                        if (terraform_formation) { 
                            //If the condition is true print the following statement 
                            sh 'terraform init'
                            sh 'terraform apply -auto-approve'
                        } else {
                            sh 'terraform destroy'
                        }
                    }
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
                sh 'terraform destroy'
            }
        }
    }
}
