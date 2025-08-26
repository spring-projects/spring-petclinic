pipeline {

    agent {label 'agent-1'}
    
    stages {
        
        stage("Checkout") {
            steps {
                checkout scm
            }
        }

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
                sh './gradlew clean build -x test -x checkstyleNohttp'
            }
        }

        stage("docker image (change request)") {
            steps {
                echo "Building Docker image for change request..."
                sh 'docker build -t spring-petclinic .'
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