pipeline {
	agent any
    tools {
        maven 'Maven 3.3.9'
        jdk 'jdk8'
    }
	stages {
		stage('Build') {
			steps {
				sh 'mvn clean'
			}
		}		
        stage('Test') { 
            steps {
                sh 'mvn test' 
            }
            post {
                always {
                    junit 'target/surefire-reports/*.xml' 
                }
            }
        }
        stage('Package') {
            steps {
                sh 'mvn package'
            }
        }
        stage('Deploy') {
            steps {
                sh 'mvn deploy'
            }
        }
	}
	post {
	    success {
	        mail to: 'sean.how0@gmail.com',
	             subject: "Build Successful: ${currentBuild.fullDisplayName}",
	             body: "Successfully built ${env.BUILD_URL}"
	    }
	    failure {
	        mail to: 'sean.how0@gmail.com',
	             subject: "Failed Pipeline: ${currentBuild.fullDisplayName}",
	             body: "Something is wrong with ${env.BUILD_URL}"
	    }
	}
}