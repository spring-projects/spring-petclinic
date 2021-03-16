pipeline {
    agent any
    stages {
        stage('checkout') {
            steps {
                git credentialsId: '0506e542-582e-411e-97bc-24e3791bb711', url: 'https://github.com/samgithubadmin/spring-petclinic.git'
            }
        }
        stage('Build') {
         steps {
             sh 'mvn clean package'
            }
        }
        stage('Junit test') {
            steps {
                junit 'target/surefire-reports/*.xml'
            }
        }
        stage('s3 bucket') {
            steps {
                s3Upload consoleLogLevel: 'INFO', dontSetBuildResultOnFailure: false, dontWaitForConcurrentBuildCompletion: false, entries: [[bucket: 'devenv12', excludedFile: '', flatten: false, gzipFiles: false, keepForever: false, managedArtifacts: false, noUploadOnFailure: false, selectedRegion: 'us-east-2', showDirectlyInBrowser: false, sourceFile: '*/**.jar', storageClass: 'STANDARD', uploadFromSlave: false, useServerSideEncryption: false]], pluginFailureResultConstraint: 'FAILURE', profileName: 's3bucket', userMetadata: []
            }
        }
    }}
