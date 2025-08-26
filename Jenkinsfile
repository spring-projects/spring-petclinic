pipeline {

    agent {label 'agent-1'}
    
    stages {

        stage("checkstyle") {
            when {
                changeRequest()
            }
            steps {
                echo "Checkstyle..."
                sh './gradlew checkstyleMain'
                archiveArtifacts artifacts: 'checkstyle.xml'
            }
        }

        stage("test") {
            when {
                changeRequest()
            }
            steps {
                echo "Testing..."
                sh './gradlew clean test'
            }
        }
        
        stage("build") {
            steps {
                echo "Building..."
                sh './gradlew clean build -x test'
            }
        }

        stage("docker image (change request)") {
            when {
                changeRequest()
            }
            steps {
                echo "Building Docker image for change request..."
            }
        }

        stage("docker image (main)") {
            when {
                branch 'main'
            }
            steps {
                echo "Building Docker image for main..."
            }
        }

    }

}