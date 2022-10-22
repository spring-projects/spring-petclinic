def call(){
    pipeline {
    agent  { label 'NODE' }
    stages {
        stage('git') {
            steps {
                git branch: "main", 
                url: 'https://github.com/gopivurata/spring-petclinic.git'
            }

        }
        stage('build') {
            steps {
                sh "/usr/share/maven/bin/mvn package"
            }
        }
        stage('archive results') {
            steps {
                junit '**/surefire-reports/*.xml'
            }
        }
    }

}
}
