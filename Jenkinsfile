pipeline{
    agent any
    stages {
        stage ("build") {
            when {
                expression {
                    return env.CHANGE_ID != null
                }
            }
            steps {
                echo "Running build automation..."
                sh './mvnw checkstyle:checkstyle'
                sh './mvnw verify'
                sh './mvnw clean package -DskipTests=true'
            }
        }
        stage ("Build Docker Image") {
            when {
                expression {
                    return env.CHANGE_ID != null
                }
            }
            steps {
                script{
                    app = docker.build("surtexx/mr:${GIT_COMMIT[0..7]}", "-f Dockerfile1 .")
                }
            }
        }
        stage ("Push Docker Image") {
            when {
                expression {
                    return env.CHANGE_ID != null
                }
            }
            steps {
                script{
                    docker.withRegistry('https://registry.hub.docker.com', 'docker_hub_login') {
                        app.push("${GIT_COMMIT[0..7]}")
                        app.push("latest")
                    }
                }
            }
        }
    }
}

pipeline{
    agent any
    stages{
        stage ("Build Docker Image") {
            when{
                branch: 'main'
            }
            steps {
                script{
                    app = docker.build("surtexx/main", "-f Dockerfile1 .")
                }
            }
        }
        stage ("Push Docker Image") {
            when{
                branch: 'main'
            }
            steps {
                script{
                    docker.withRegistry('https://registry.hub.docker.com', 'docker_hub_login') {
                        app.push("latest")
                    }
                }
            }
        }
    }
}
