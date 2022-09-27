pipeline {
    agent any
    stages {
        stage('test') {
            steps {
                sh 'echo hello'
            }
        }
        stage('learning') {
            steps {
                git url: 'https://github.com/srikanthreddygajjala070/spring-petclinic.git', 
                    branch: 'NEW_BRANCH'
            }
            stage('build') {
                steps {
                    sh 'mvn package'
                }
            }
        }
    }
}
