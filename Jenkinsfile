pipeline { 
    agent any 
    options {
        skipStagesAfterUnstable()
    }
    stages {
        stage('Build') { 
            steps { 
                sh './mvnw package' 
            }
        }
        stage('Deploy') { 
            steps { 
                sh 'java -jar target/*.jar' 
            }
        }
    }
}