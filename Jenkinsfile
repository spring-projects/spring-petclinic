pipeline {
    agent any

    stages {
        stage ('Initialize') {
            steps {
                sh '''
                    echo "PATH = ${PATH}"
                    echo "M2_HOME = ${M2_HOME}"
                '''
            }
        }
        
        stage('Build') {
            steps {
                echo 'Building..'
                sh 'docker login -u="$DOCKER_USER" -p="$DOCKER_PASS"'
                sh 'mvn -q package'
                sh 'docker build -t pet-clinic .'
                sh 'docker tag pet-clinic $DOCKER_USER/pet-clinic:latest'
                sh 'docker push $DOCKER_USER/pet-clinic:latest'
            }
        }
       

        stage('Run local container') {
          agent any
          steps {
            sh 'docker rm -f natyramone/pet-clinic:latest || true'
            sh 'docker run -d -p 8081:8080 natyramone/pet-clinic:latest'
          }
        }
        
        stage('Push to dockerhub') {
          agent any
          steps {
                sh 'docker push $DOCKER_USER/pet-clinic:latest'
              }
        }
      
    
    }
}

