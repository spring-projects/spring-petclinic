pipeline {
    agent { docker { image 'maven:3.3.3' } }
    stages {
        stage('build and test') {
            steps {
                echo 'Building..'
            }
            steps {
                sh 'mvn package'
            }
        }
        stage('docker image build') {
            steps {
                echo 'Docker compose..'
            }
        	steps {
        		sh 'docker-compose down && docker-compose up --build -d'
        	}
        }
    }

    post {  
        failure {  
             mail bcc: '', body: "<b>Example</b><br>\n\<br>Project: ${env.JOB_NAME} <br>Build Number: ${env.BUILD_NUMBER} <br> URL de build: ${env.BUILD_URL}", cc: '', charset: 'UTF-8', from: '', mimeType: 'text/html', replyTo: '', subject: "ERROR CI: Project name -> ${env.JOB_NAME}", to: "foo@gmail.com";  
        }    
    }

}