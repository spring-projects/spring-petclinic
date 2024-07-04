pipeline {
    agent none
    stages {
        stage('Build') {
            agent {
                label 'builder'
            }
            steps {
                sh '''
                    git clone https://github.com/KeraLLenarium/spring-petclinic.git
                    cd spring-petclinic
                    ./mvnw spring-boot:build-image
                    docker tag spring-petclinic:3.3.0-SNAPSHOT kerallenarium/spring-petclinic
                    docker push kerallenarium/spring-petclinic
                    docker rmi -f $(docker images -q)
                '''
            }
            post {
                always {
                    sh 'rm -rf spring-petclinic'
                }
            }
        }
        stage('Deploy') {
            agent {
                label 'tester'
            }
            steps {
                sh '''
                    docker pull kerallenarium/spring-petclinic:latest
                    if [ "$(docker ps -q)" ]; then
                        docker stop $(docker ps -q)
                    fi
                    java --version
                    docker run -d -p 80:8080 kerallenarium/spring-petclinic:latest
                '''
            }
        }
    }
}
