pipeline{
    agent any
    stages {
        stage ("build") {
            steps {
                echo "Running build automation..."
                sh './mvnw checkstyle:checkstyle'
                sh './mvnw verify'
                sh './mvnw clean package'
                sh 'docker build -t mr/spring-petclinic:${GIT_COMMIT:0:7} -f Dockerfile2 .'
            }
        }
    }
}
