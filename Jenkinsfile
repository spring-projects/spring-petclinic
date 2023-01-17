pipeline {

    environment { 
        DOCKERHUB_CREDENTIALS=credentials('rolandgryddynamics-dockerhub')
        MERGE_REPOSITORY_NAME='mr'
    }

    agent {
        node {
            label 'ubuntu-master'
        }
    }
    stages {
        // stage('checkstyle') {
        //     steps {
        //         sh './gradlew checkstyleMain'
        //         archiveArtifacts artifacts: 'build/reports/checkstyle/main.html'
        //     }
        // }
        // stage('test') {
        //     steps {
        //         sh './gradlew compileJava'
        //     }
        // }
        // stage('build') {
        //     steps {
        //         sh './gradlew build -x test'
        //     }
        // }
 
        stage('Build Docker image') {
            steps {
                sh 'docker build -t $DOCKERHUB_CREDENTIALS_USR/$MERGE_REPOSITORY_NAME:$BUILD_NUMBER .'
            }
        }
        stage('Login DockerHub') {
            steps {
                sh 'docker login -u $DOCKERHUB_CREDENTIALS_USR -p $DOCKERHUB_CREDENTIALS_PSW'
            }
        }
        stage('Deploy Docker image to DockerHub') {
            steps {
                sh 'docker push $DOCKERHUB_CREDENTIALS_USR/$MERGE_REPOSITORY_NAME:$BUILD_NUMBER'
            }
        }
    }
    post {
        always {
            sh 'docker logout'
        }
    }
}