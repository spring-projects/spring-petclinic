pipeline {
    agent any
    stages {
        stage('checkout') {
            steps {
        git 'https://github.com/cjpcloud/spring-petclinic.git'
            }
        }
        stage('build&Analysis') { 
            steps {
                sh label: '', script: '''mvn clean package sonar:sonar \\
                         -Dsonar.projectKey=java \\
                         -Dsonar.host.url=http://3.236.21.127:9002 \\
                       -Dsonar.login=cda8dbcff84a5229b18f410e9c3b5c754de88a21'''
            } 
            
        }
        stage('Junit') {
        steps {
            junit '**/target/surefire-reports/*.xml'
        }
    }
        stage('artifact deploy to s3') {
        steps {
            s3Upload consoleLogLevel: 'INFO', dontSetBuildResultOnFailure: false, dontWaitForConcurrentBuildCompletion: false, entries: [[bucket: 'bucketp96', excludedFile: '', flatten: false, gzipFiles: false, keepForever: false, managedArtifacts: false, noUploadOnFailure: false, selectedRegion: 'us-east-1', showDirectlyInBrowser: false, sourceFile: '**/*.jar', storageClass: 'STANDARD', uploadFromSlave: false, useServerSideEncryption: false]], pluginFailureResultConstraint: 'SUCCESS', profileName: 's3upload', userMetadata: []
        }
    }
    }}
