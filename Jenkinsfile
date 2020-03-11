pipeline {
    agent any
    
    stages {
        stage('Build') {
            when { branch 'master' }
            steps {
                sh './mvnw compile' 
            }
        }
        
        stage('Test') {
            when { branch 'master' }
            steps {
                sh './mvnw test' 
            }
        }
        
        stage('Package') {
            when { branch 'master' }
            steps {
                sh './mvnw package' 
            }
        }
        
        stage('Deploy') {
            when { branch 'master' }
            steps {
                sh './mvnw deploy' 
            }
        }
    }
}
