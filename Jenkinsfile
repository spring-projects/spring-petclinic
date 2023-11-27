pipeline {
    agent any
    stages {
        stage('Checkstyle') {
            steps {
                sh 'mvn checkstyle:checkstyle'
                archiveArtifacts artifacts: 'target/checkstyle-result.html', onlyIfSuccessful: true
            }
        }
        stage('Tests') {
            steps {
                echo "now we will begin the tests"
            }
        }
        stage('Build') {
            steps {
                echo "now we will begin the build"
            }
        }
        stage('Create docker image') {
            steps {
                echo "now we will begin the creation of the docker image"
            }
        }
        stage('Tag the docker image') {
            steps {
                echo "now we will tag the docker image"
            }
        }
        stage('Push to DockerHub') {
            steps {
                echo "now we will push to the docker file"
            }
        }
        stage('Change stages') {
            steps {
                echo "this is the start of the new stage with the main branch"
            }
        }
        stage('Create docker image for main branch') {
            steps {
                echo "now we will create the docker image for the main branch"
            }
        }
        stage('Push docker image to main repository') {
            steps {
                echo "now we will push the image to the docker main repository"
            }
        }
    }
}
