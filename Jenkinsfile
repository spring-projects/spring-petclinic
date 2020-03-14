pipeline { 
	agent any 
	stages { 
		stage('Build') { 
			steps { 
				sh 'mvn clean compile'
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