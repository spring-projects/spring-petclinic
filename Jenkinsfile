pipeline{
    agent any
    stages {
        stage ("build") {
            steps {
                echo "Running build automation..."
                sh './mvnw checkstyle:checkstyle'
                sh './mvnw verify'
                sh './mvnw clean package -DskipTests=true'
            }
        }
        stage ("Build Docker Image") {
            steps {
                script{
                    app = docker.build("surtexx/mr:${GIT_COMMIT}", "-f Dockerfile2 .")
                    app.inside{
                        sh 'echo $(curl localhostt:8080)'
                    }
                }
            }
        }
        stage ("Push Docker Image") {
            steps {
                script{
                    docker.withRegistry('https://registry.hub.docker.com', 'docker_hub_login') {
                        app.push(${GIT_COMMIT})
                        app.push("latest")
                    }
                }
            }
        }
    }
}
