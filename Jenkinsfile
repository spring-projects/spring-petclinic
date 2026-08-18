pipeline {
    agent any
    tools {
        maven 'MAVEN_3.9.16'
    }
    triggers {
        pollSCM('* * * * *')
    }
    parameters {
        string(name: 'GOALS', defaultValue: 'clean package', description: 'Maven goals to execute')
    }
    stages {
        stage('SCM') {
            steps {
                git url: 'https://github.com/spring-projects/spring-petclinic.git',
                    branch: 'main'
            }
        }
        stage('BUILD') {
            steps {
                sh "mvn ${params.GOALS}"
            }
        }
       
    }
}