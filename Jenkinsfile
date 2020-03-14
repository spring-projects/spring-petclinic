pipeline { 
	agent any 
	stages { 
		stage('Build') { 
			steps { 
				sh 'mvn clean build'
			} 
		}
		stage('Test'){
			steps { 
				sh 'mvn test'
			} 
		}
		stage('package'){
			steps { 
				sh './mvnw package'
			}
		}
		stage('deploy'){
			steps { 
				echo 'deploying'
			}
		}
	} 
}