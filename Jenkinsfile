pipeline{
    agent any
    stages {
        stage ("build") {
            steps {
                echo "Running build automation..."
                sh 'mvn checkstyle:checkstyle'
                sh 'mvn verify'
                sh 'mvn clean package'
                sh 'docker build -t mr/spring-petclinic:${GIT_COMMIT:0:7} -f Dockerfile2 .'
            }
        }
    }
}