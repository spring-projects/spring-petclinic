
stage('Download and Execute Jenkinsfile') {
    steps {
        // Download the Jenkinsfile from S3
        withAWS(region: 'ap-south-1', credentials: 'iamuser1') {
            sh 'aws s3 cp s3://myjenkinsbucket001/Jenkinsfile /tmp/Jenkinsfile'
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


