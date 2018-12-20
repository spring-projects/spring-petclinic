pipeline {
    agent any

    stages {
        stage ('Initialize') {
            steps {
                echo 'Docker login..'
                sh 'docker login -u="$DOCKER_USER" -p="$DOCKER_PASS"'
            }
        }
        
        stage('Build') {
            steps {
                echo 'Building..'
                
                sh 'mvn -q package'
            }
        }
        
      stage('Sonar') {
        steps {
            echo 'Running Sonar..'
            sh 'mvn sonar:sonar -Dsonar.projectKey=villegasnaty_spring-petclinic -Dsonar.organization=villegasnaty-github -Dsonar.host.url=https://sonarcloud.io -Dsonar.login=5f3fd10f7702318ee919ea4d55890502510b2448'
        }
     }
     
     stage('Build Container') {
            steps {
                echo 'Building Docker Image..'
                sh 'docker rmi --force "natyramone/pet-clinic"'
                sh 'docker build -t pet-clinic .'
                sh 'docker tag pet-clinic $DOCKER_USER/pet-clinic:latest'
            }
        }
       

        stage('Run local container') {
          agent any
          steps {
          
            sh 'docker rm -f pet-clinic || true'
            sh 'docker run -d -p 8081:8080 --name pet-clinic natyramone/pet-clinic:latest'
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

