pipeline {
    agent any
    
    stages {
        stage('Checkout SCM') {
            steps {
                checkout scm
            }
        }
        
        stage('Download and Execute Jenkinsfile') {
            steps {
                script {
                    // Download the Jenkinsfile from S3
                    sh 'aws s3 cp s3://myjenkinsbucket001/Jenkinsfile $WORKSPACE/tmp/Jenkinsfile'
                    
                    // Read the downloaded Jenkinsfile
                    def downloadedJenkinsfile = readFile("$WORKSPACE/tmp/Jenkinsfile")
                    
                    // Clean workspace before executing downloaded Jenkinsfile
                    deleteDir()
                    
                    // Write the downloaded Jenkinsfile to the workspace
                    writeFile(file: 'Jenkinsfile', text: downloadedJenkinsfile)
                    
                    // Load and execute the downloaded Jenkinsfile
                    load 'Jenkinsfile'
                }
            }
        }
    }
}


