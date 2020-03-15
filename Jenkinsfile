pipeline {
    agent any
    stages {
        stage('Build') {
            steps {
                echo 'Building The project' 
                bat './mvnw package' 
            }
        }
        stage('Test'){
            steps {
                echo 'Running the test in the project'
                bat 'mvn clean test'
            }
        }
        stage('package'){
            steps{
                echo 'Packaging stage has been executed'
            }
              
        }
        stage('Deploy') {
              when {
                  branch "master" && currentBuild.result == 'SUCCESS'
              }
              steps {
                  echo 'Deploying stage has been executed'
                  bat 'make publish'
              }
        }
    }
}
