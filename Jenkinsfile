pipeline { 
    agent any
      environment {
    PATH = "C:\\WINDOWS\\SYSTEM32"
}
    stages {
        
        stage('Build') { 
            steps { 
                echo "Building..."
                 bat "./mvnw clean"
                 bat "./mvnw compile" 
            }
        }
                             
        stage ('Test') {

            steps {
                echo "Testing..."
                 bat "./mvnw test"              
            }
        }
        
        stage('Package') { 
            steps {
                echo "Packaging..."
                 bat "./mvnw package" 
            }
        }
        
        stage('Deploy') {
            steps {
                echo "Deploying..."
                echo "./mvnw deploy with distributionManagement set in pom.xml"
            }
        }
    }
}
