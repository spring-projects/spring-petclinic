pipeline {
  agent any
    environment {
    HEROKU_API_KEY = credentials('brobert-heroku-api-key')
  }
  parameters { 
    string(name: 'APP_NAME', defaultValue: 'pet-clinic-devops-brobert', description: 'What is the Heroku app name?') 
  }
  stages {
    stage('init') {
      steps {
        checkout scm
      }
    }

    stage('Build project') {
      steps {
        sh 'mvn clean'
      }
    }

    stage('Scan') {
      steps {
        withSonarQubeEnv('sq') {
          sh './mvnw clean install org.sonarsource.scanner.maven:sonar-maven-plugin:3.9.0.2155:sonar'
        }

      }
    }

    stage('SQuality Gate') {
      steps {
        timeout(time: 1, unit: 'MINUTES') {
          waitForQualityGate true
        }

      }
    }

    stage('Package') {
      steps {
        sh 'mvn package'
      }
    }
    
        stage('Build') {
      steps {
        sh 'docker build -t brobert/devops-pet-clinic:latest .'
      }
    }
    stage('Login') {
      steps {
        sh 'echo $HEROKU_API_KEY | docker login --username=_ --password-stdin registry.heroku.com'
      }
    }
    stage('Push to Heroku registry') {
      steps {
        sh '''
          docker tag brobert/devops-pet-clinic:latest registry.heroku.com/$APP_NAME/web
          docker push registry.heroku.com/$APP_NAME/web
        '''
      }
    }
    stage('Release the image') {
      steps {
        sh '''
          heroku container:release web --app=$APP_NAME
        '''
      }
    }

//     stage('Move JAR file') {
//       steps {
//         sh 'sudo mkdir -p /home/ubuntu/petclinic-deploy/'
//         sh 'sudo cp target/spring-petclinic-2.7.0-SNAPSHOT.jar /home/ubuntu/petclinic-deploy/'
//       }
//     }



  }
  tools {
    maven 'maven'
    jdk 'java11'
  }
}
