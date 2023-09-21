pipeline{
    agent any
    
    tools{
        maven "maven3"
    }
    /*
    triggers{
        cron("* * * * *")
    }
    */
    stages{
        stage("Checkout"){
            steps{
                echo "========executing checkout========"
                git url:"https://github.com/A-hash-bit/spring-petclinic.git", branch:"main"
            }
                       
        }
    
        stage("Build"){
            steps{
                sh "mvn clean package"
            }
        }
       stage('Docker Build') {
         agent any
          steps {
              echo "========executing docker build========"
              sh 'docker build -t amar1doc/petclinic:latest .'

              // with dockerhub creds to push to registory
              //  script{
              //     withDockerRegistry(credentialsId: 'dockerhub') {
              //       sh 'docker build -t amar1doc/petclinic:${BUILD_NUMBER} .'
              //       sh 'docker push amar1doc/petclinic:${BUILD_NUMBER}'
              //    }
              // }
            }
    }
    }
    post{
        always{
            echo "========always========"
        }
        success{
            echo "========pipeline executed successfully ========"
        }
        failure{
            echo "========pipeline execution failed========"
        }
    }
}

