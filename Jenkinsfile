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
        
      stage('Sonar') {
        steps {
            sh 'mvn sonar:sonar -Dsonar.projectKey=villegasnaty_spring-petclinic -Dsonar.organization=villegasnaty-github -Dsonar.host.url=https://sonarcloud.io -Dsonar.login=5f3fd10f7702318ee919ea4d55890502510b2448'
        }
     }
     
     stage('Build Container') {
            steps {
                echo 'Building..'
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
        
        stage('Run Integration Test') {
          agent any
          steps {
            sh 'mvn clean verify -Dbrowser=chrome'
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

