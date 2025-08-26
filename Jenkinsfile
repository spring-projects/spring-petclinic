pipeline {

    agent {label 'agent-1'}
    
    stages {
        
        stage("Checkout") {
            steps {
                checkout scm
            }
        }

        stage("checkstyle") {
            steps {
                echo "Checkstyle..."
                sh './gradlew checkstyleMain'
                archiveArtifacts artifacts: 'build/reports/checkstyle/*.xml', allowEmptyArchive: true
                sh 'ls -la'
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
            when {
                changeRequest()
            }
            steps {
                echo "Building..."
                sh './gradlew clean build -x test -x checkstyleNohttp'
            }
        }

        stage("docker image (change request)") {
            when {
                changeRequest()
            }
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
                sh 'docker build -t spring-petclinic .'
            }
        }

    }

}