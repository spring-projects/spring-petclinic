pipeline {
    agent { label 'MAVEN' }  // Runs on agent with label "MAVEN"

    options {
        timeout(time: 1, unit: 'HOURS')  // Fail job if it exceeds 1 hour
    }

    triggers {
        cron('* * * * *')  // Runs every minute
    }

    stages {
        stage('Git Checkout') {
            steps {
                git branch: 'main', url: 'https://github.com/srikanthkunaofficial/spring-petclinic.git'
            }
        }

        stage('Build with Maven') {
            steps {
                sh "mvn package"
            }
        }
    }
}
