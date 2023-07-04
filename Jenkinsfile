pipeline {
    agent any

    stages {
        stage('Download and Execute Shell Script') {
            steps {
                withAWS(region: 'ap-south-1', credentials: 'iamuser1') {
                    script {
                        // Download the shell script from S3
                        sh 'aws s3 cp s3://myjenkinsbucket001/checkout/checkout.sh /tmp/script.sh'
                    }
                }

                script {
                    // Make the downloaded script executable
                    sh 'chmod +x /tmp/script.sh'
                }
            }
        }

        stage('Execute Downloaded Shell Script') {
            steps {
                // Execute the downloaded shell script
                sh '/tmp/script.sh'
            }
        }
    }
}

