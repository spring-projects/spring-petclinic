pipeline {

    agent any
    
    stages {

        stage("checkstyle") {
            steps {
                echo "Checkstyle..."
                sh './gradlew checkstyleMain'
            }
        }

        stage("test") {
            steps {
                echo "Testing..."
                sh './gradlew test'
            }
        }
        
        stage("build") {
            steps {
                echo "Building..."
                sh './gradlew clean build -x test'
            }
        }

        stage("dockerImage") {
            steps {
                echo "Building Docker image..."
            }
        }

    }

}