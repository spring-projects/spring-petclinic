pipeline { 
	agent any 
	stages { 
		stage('Build') { 
			steps { 
				slackSend (color: '#00FF00', message: "Building: Job '${env.JOB_NAME} [${env.BUILD_NUMBER}]' (${env.BUILD_URL})")
				sh 'mvn clean install'
			} 
		}
		stage('Test'){
			steps { 
				slackSend (color: '#00FF00', message: "Testing: Job '${env.JOB_NAME} [${env.BUILD_NUMBER}]' (${env.BUILD_URL})")
				sh 'mvn test'
			} 
		}
		stage('package'){
			steps { 
				slackSend (color: '#00FF00', message: "Packaging: Job '${env.JOB_NAME} [${env.BUILD_NUMBER}]' (${env.BUILD_URL})")
				sh './mvnw package'
				
			}
		}
		stage('deploy'){
			steps { 
				slackSend (color: '#00FF00', message: "Deploying: Job '${env.JOB_NAME} [${env.BUILD_NUMBER}]' (${env.BUILD_URL})")
				sh './mvnw deploy -DaltDeploymentRepository=internal.repo::default::"file:///mnt/c/Users/Fgrcl/My Cloud/Semester 6/SOEN  345/ASSIGNMENTS/a6/Jenkins/Deploy"'
			}
		}
	}
	post {
		success {
		  slackSend (color: '#00FF00', message: "SUCCESSFUL: Job '${env.JOB_NAME} [${env.BUILD_NUMBER}]' (${env.BUILD_URL})")
		}

		failure {
		  slackSend (color: '#FF0000', message: "FAILED: Job '${env.JOB_NAME} [${env.BUILD_NUMBER}]' (${env.BUILD_URL})")
		}
	}	
}