pipeline {
    agent any

    triggers {
        pollSCM '*/5 * * * *'
    }

    stages {
        stage('Checkstyle') {
            steps {
                sh '''
                    ./mvnw checkstyle:checkstyle
                '''
            }
        }

        stage('Test') {
            steps {
                sh '''
                    ./mvnw test
                '''
            }
        }

        stage('Build') {
            steps {
                sh '''
                    ./mvnw clean package
                '''
            }
        }

        stage('Docker up') {
            steps {
                sh '''
                    docker build -t "gavetisyangd/mr:${GIT_COMMIT}" ./ 
                '''
            }
        }

        stage('Push') {
            steps {
                withCredentials([string(credentialsId: 'dhub', variable: 'TOKEN')]) {
                    sh '''
                        echo $TOKEN | docker login -u gavetisyangd --password-stdin
                        docker push "gavetisyangd/mr:${GIT_COMMIT}"
                    '''
                }
            }
        }
    }
}
