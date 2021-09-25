pipeline {
    agent any
    
    stages {
        stage("Build jartifact") {
            steps {
                echo "=============== Building starts =================="
                sh "pwd"
                sh "./mvnw package"
            }
            
        }
        
    }

}
