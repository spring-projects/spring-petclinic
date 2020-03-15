pipeline { 
	agent any 
	stages { 
		stage('Build') { 
			steps { 
				sh 'mvn clean compile'
				emailext (
					subject: "Starting: Job '${env.JOB_NAME} [${env.BUILD_NUMBER}]'",
					body: """<p>start: Job '${env.JOB_NAME} [${env.BUILD_NUMBER}]':</p>
					  <p>Check console output at &QUOT;<a href='${env.BUILD_URL}'>${env.JOB_NAME} [${env.BUILD_NUMBER}]</a>&QUOT;</p>""",
					recipientProviders: [[$class: 'DevelopersRecipientProvider']]
				)
			} 
		}
		stage('Test'){
			steps { 
				sh 'mvn test'
				emailext (
					subject: "Testing: Job '${env.JOB_NAME} [${env.BUILD_NUMBER}]'",
					body: """<p>Test: Job '${env.JOB_NAME} [${env.BUILD_NUMBER}]':</p>
					  <p>Check console output at &QUOT;<a href='${env.BUILD_URL}'>${env.JOB_NAME} [${env.BUILD_NUMBER}]</a>&QUOT;</p>""",
					recipientProviders: [[$class: 'DevelopersRecipientProvider']]
				)
			} 
		}
		stage('package'){
			steps { 
				sh './mvnw package
				emailext (
					subject: "Packaging: Job '${env.JOB_NAME} [${env.BUILD_NUMBER}]'",
					body: """<p>Package: Job '${env.JOB_NAME} [${env.BUILD_NUMBER}]':</p>
					  <p>Check console output at &QUOT;<a href='${env.BUILD_URL}'>${env.JOB_NAME} [${env.BUILD_NUMBER}]</a>&QUOT;</p>""",
					recipientProviders: [[$class: 'DevelopersRecipientProvider']]
				)
				
			}
		}
		stage('deploy'){
			steps { 
				echo 'deploying'
				emailext (
					subject: "Deploying: Job '${env.JOB_NAME} [${env.BUILD_NUMBER}]'",
					body: """<p>Deploy: Job '${env.JOB_NAME} [${env.BUILD_NUMBER}]':</p>
					  <p>Check console output at &QUOT;<a href='${env.BUILD_URL}'>${env.JOB_NAME} [${env.BUILD_NUMBER}]</a>&QUOT;</p>""",
					recipientProviders: [[$class: 'DevelopersRecipientProvider']]
				)
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