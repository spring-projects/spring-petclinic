pipeline {

    environment { 
        DOCKERHUB_CREDENTIALS=credentials('rolandgryddynamics-dockerhub')
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
 
        // stage('docker') {
        //     steps {
        //             // sh 'docker version'
        //             // app = docker.build("rolandgryddynamics/mr")
        //             sh 'docker build -t rolandgryddynamics/mr:latest .'
                    
        //             // sh 'docker tag my/app rolandgryddynamics/mr'
        //             // sh 'docker tag push rolandgryddynamics/mr'
        //     }
        // }
        stage('login') {
            steps {
                sh 'docker login --username $DOCKERHUB_CREDENTIALS_USR --password-stdin $DOCKERHUB_CREDENTIALS_PSW'
            }
        }
        stage('deploy to dockerhub') {
            steps {
                sh 'docker push rolandgryddynamics/mr:latest'
                // script {
                //     dockerImage = docker.withRegistry('https://registry.hub.docker.com', 'rolandgryddynamics-dockerhub' )
                //     dockerImage.push()
                // }
            }
        }
    }
}