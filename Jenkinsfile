#!groovy

pipeline {
    agent any
    }

    stages {
        stage("Build") {
            steps {
                echo "HELLOW WORKLD"
            }
        }
    }

    post {
        always {
            cleanWs()
        }
    }
}
