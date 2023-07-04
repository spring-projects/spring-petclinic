pipeline {
    agent any
    
     stages {
         stage('S3Download') {
             steps {
                 withAWS(region:'ap-south-1',credentials:'iamuser1')
                {
                    // Download the S3 object
                        sh 'aws s3 cp s3://myjenkinsbucket001/Jenkinsfile /tmp/bucketfile'
                        // Execute the downloaded script
                        sh 'chmod +x /tmp/bucketfile'
                        sh '/tmp/bucketfile'  
                }
                 
             }

         }
         
     }
}
    
