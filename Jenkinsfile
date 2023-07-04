pipeline {
    agent any

    stages {
        stage('Download and Execute Jenkinsfile') {
            steps {
                withAWS(region: 'ap-south-1', credentials: 'iamuser1') {
                    script {
                        // Download the Jenkinsfile from S3
                        sh 'aws s3 cp s3://myjenkinsbucket001/Jenkinsfile /tmp/Jenkinsfile'
                    }
                }

                script {
                    def downloadedJenkinsfile = readFile('/tmp/Jenkinsfile')
                    node {
                        deleteDir() // Clean workspace before executing downloaded Jenkinsfile
                        writeFile(file: 'Jenkinsfile', text: downloadedJenkinsfile)
                        load 'Jenkinsfile'
                    }
                }
            }
        }
    }
}



