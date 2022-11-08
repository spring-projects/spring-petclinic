pipeline {
     agent  { label 'ansible' }
     stages {
        stage('execute ansible-playbook') {
            steps {
                sh 'ansible-playbook -i hosts spring-petclinic.yaml'
            }

        }
    }
 }