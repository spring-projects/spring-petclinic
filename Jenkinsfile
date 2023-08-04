pipeline {
    agent {label 'JDK_17'}
    options {
        retry(3)
        timeout(time: 30, unit: 'MINUTES')
    }
    triggers {
        pollSCM('* * * * *')
    }
    tools {
        jdk 'JDK_17'
        maven 'maven 3.9.4'
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
                sh 'mvn package'
            }
        }
        stage('build') {
            steps {
                archiveArtifacts artifacts: '**/target/spring-petclinic-*.jar'
                junit testResults : '**/target/surefire-reports/TEST-*.xml'
            }
        }
    }
    post {
        success {
            mail subject: '${JOB_NAME}:: has completed with success',
                 body: 'your project is effective \n Build Url ${BUILD_URL',
                 to: 'vinod@gmail.com'
        }
        failure {
            mail subject: '${JOB_NAME}:: has completed with failed',
                 body: 'your project is defective \n Build Url ${BUILD_URL',
                 to: 'vinod@gmail.com'
        }
    }
}
