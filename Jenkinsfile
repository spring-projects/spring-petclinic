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
                    sh 'docker version'
                    app = docker.build("rolandgryddynamics/mr")
                    // sh 'docker build -t my/app .'
                    
                    // sh 'docker tag my/app rolandgryddynamics/mr'
                    // sh 'docker tag push rolandgryddynamics/mr'
                }
            }
        }
        stage('deploy to dockerhub') {
            steps {
                script {
                    echo 'jelllkdskdks'
                    docker.withRegistry("https://registry.hub.docker.com", "webserver_login")
                    app.push("${env.BUILD_NUMBER}")
                    app.push("latest")
                }
            }
        }
    }
}