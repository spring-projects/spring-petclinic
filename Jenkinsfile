pipeline {
    agent any
    
     stages {
         stage('S3Download') {
             steps {
                 withAWS(region:'ap-south-1',credentials:'iamuser1')\
                {
                    s3Download(file: 'Jenkinsfile', bucket: 'myjenkinsbucket001', path: 'https://myjenkinsbucket001.s3.ap-south-1.amazonaws.com/Jenkinsfile')
                }
                 
             }

         }
         stage('GitCheckout'){
             steps {
                 git 'https://github.com/saisrinisrinivas/spring-petclinic.git'
             }

         }
         stage('Run'){
             steps {
                 sh 'mvn package'
             }
         }
     }
}
    
