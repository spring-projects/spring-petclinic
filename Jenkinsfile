pipeline {

    agent any
    
    stages {

        stage("checkstyle") {
            steps {
                echo "Checkstyle..."
            }
        }

        stage("test") {
            steps {
                echo "Testing..."
            }
        }
        
        stage("build") {
            steps {
                echo "Building..."
            }
        }

        stage("dockerImage") {
            steps {
                echo "Building Docker image..."
            }
        }

    }

}