pipeline { 
	agent any 
	stages { 
		stage('Build') { 
			steps { 
				emailext (
					subject: "Starting: Job '${env.JOB_NAME} [${env.BUILD_NUMBER}]'",
					body: """<p>start: Job '${env.JOB_NAME} [${env.BUILD_NUMBER}]':</p>
					  <p>Check console output at &QUOT;<a href='${env.BUILD_URL}'>${env.JOB_NAME} [${env.BUILD_NUMBER}]</a>&QUOT;</p>""",
					recipientProviders: [[$class: 'DevelopersRecipientProvider']]
				)
				sh 'mvn clean install'
			} 
		}
		stage('Test'){
			steps { 
				emailext (
					subject: "Testing: Job '${env.JOB_NAME} [${env.BUILD_NUMBER}]'",
					body: """<p>Test: Job '${env.JOB_NAME} [${env.BUILD_NUMBER}]':</p>
					  <p>Check console output at &QUOT;<a href='${env.BUILD_URL}'>${env.JOB_NAME} [${env.BUILD_NUMBER}]</a>&QUOT;</p>""",
					recipientProviders: [[$class: 'DevelopersRecipientProvider']]
				)
				sh 'mvn test'
			} 
		}
		stage('package'){
			steps { 
				emailext (
					subject: "Packaging: Job '${env.JOB_NAME} [${env.BUILD_NUMBER}]'",
					body: """<p>Package: Job '${env.JOB_NAME} [${env.BUILD_NUMBER}]':</p>
					  <p>Check console output at &QUOT;<a href='${env.BUILD_URL}'>${env.JOB_NAME} [${env.BUILD_NUMBER}]</a>&QUOT;</p>""",
					recipientProviders: [[$class: 'DevelopersRecipientProvider']]
				)
				sh './mvnw package'
				
			}
		}
		stage('deploy'){
			steps { 
				emailext (
					subject: "Deploying: Job '${env.JOB_NAME} [${env.BUILD_NUMBER}]'",
					body: """<p>Deploy: Job '${env.JOB_NAME} [${env.BUILD_NUMBER}]':</p>
					  <p>Check console output at &QUOT;<a href='${env.BUILD_URL}'>${env.JOB_NAME} [${env.BUILD_NUMBER}]</a>&QUOT;</p>""",
					recipientProviders: [[$class: 'DevelopersRecipientProvider']]
				)
				sh './mvnw deploy -DaltDeploymentRepository=internal.repo::default::file:///mnt/c/Users/Fgrcl/My Cloud/Semester 6/SOEN  345/ASSIGNMENTS/a6/Jenkins/Deploy'
			}
		}
	}
	post {
		success {
		  slackSend (color: '#00FF00', message: "SUCCESSFUL: Job '${env.JOB_NAME} [${env.BUILD_NUMBER}]' (${env.BUILD_URL})")

		  hipchatSend (color: 'GREEN', notify: true,
			  message: "SUCCESSFUL: Job '${env.JOB_NAME} [${env.BUILD_NUMBER}]' (${env.BUILD_URL})"
			)

		  emailext (
			  subject: "SUCCESSFUL: Job '${env.JOB_NAME} [${env.BUILD_NUMBER}]'",
			  body: """<p>SUCCESSFUL: Job '${env.JOB_NAME} [${env.BUILD_NUMBER}]':</p>
				<p>Check console output at &QUOT;<a href='${env.BUILD_URL}'>${env.JOB_NAME} [${env.BUILD_NUMBER}]</a>&QUOT;</p>""",
			  recipientProviders: [[$class: 'DevelopersRecipientProvider']]
			)
		}

		failure {
		  slackSend (color: '#FF0000', message: "FAILED: Job '${env.JOB_NAME} [${env.BUILD_NUMBER}]' (${env.BUILD_URL})")

		  hipchatSend (color: 'RED', notify: true,
			  message: "FAILED: Job '${env.JOB_NAME} [${env.BUILD_NUMBER}]' (${env.BUILD_URL})"
			)

		  emailext (
			  subject: "FAILED: Job '${env.JOB_NAME} [${env.BUILD_NUMBER}]'",
			  body: """<p>FAILED: Job '${env.JOB_NAME} [${env.BUILD_NUMBER}]':</p>
				<p>Check console output at &QUOT;<a href='${env.BUILD_URL}'>${env.JOB_NAME} [${env.BUILD_NUMBER}]</a>&QUOT;</p>""",
			  recipientProviders: [[$class: 'DevelopersRecipientProvider']]
			)
		}
	}	
}