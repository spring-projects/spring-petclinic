pipeline {
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
        stage('docker') {
            steps {
                script {
                    app = docker.build("rolandgryddynamics/mr")
                    // sh 'docker build -t my/app .'
                    sh 'ls -a'
                    sh 'docker version'
                    // sh 'docker tag my/app rolandgryddynamics/mr'
                    // sh 'docker tag push rolandgryddynamics/mr'
                }
            }
        }
        stage('deploy to dockerhub') {
            steps {
                script {
                    docker.withRegistry("https://registry.hub.docker.com", "webserver_login")
                    app.push("latest")
                }
            }
        }
    }
}