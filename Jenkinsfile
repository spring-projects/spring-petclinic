pipeline {
    agent {label 'JDK_8'}
    options {
        retry(3)
        timeout(time: 30, unit: 'MINUTES')
    }
    triggers {
        pollSCM('* * * * *')
    }
    tools {
        jdk 'JAVA_8'
    }
    stages {
        stage('code') {
            steps {
                git url: 'https://github.com/KVKR31/dummy.git' ,
                branch : 'main'
            }
        }
        stage('package') {
            steps {
                sh script: ' mvn clean package'
            }
        }
        stage('build') {
            steps {
                junit testResults: '**/surefire-reports/TEST-*.xml'
                archiveArtifacts artifacts: '**/target/gameoflife.war'
            }
        }
    }
    pool {
        success {
            mail subject: 'your project is effective'
                 body: 'your project is effective'
                 to: 'vinod@gmail.com'
        }
        failure {
            mail subject: 'your project is failure'
                 body: 'your project is defective'
                 to: 'vinod@gmail.com'
        }
    }
}