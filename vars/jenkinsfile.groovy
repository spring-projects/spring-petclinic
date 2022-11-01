def call() {
    pipeline {
        agent any
        stages {
            stage('clone') {     
                steps {
                    git url: 'https://github.com/GUDAPATIVENKATESH/spring-petclinic.git',
                    branch: 'main'
                }
            }
            stage('build') { 
                steps {
                    sh 'mvn package'
                }
            }
        }
    }
}