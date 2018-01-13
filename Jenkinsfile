pipeline {
	tools {
	    maven 'Maven 3.5.0'
	}

    agent any

    stages {
        stage('Build, Test, and Package') {
            steps {
                sh "mvn clean package"
            }
        }
		
        stage('SonarQube Analysis') {
			steps {
				startSonarQubeAnalysis "SonarQube_Scanner_3.0.3.778", "Staging", "${env.WORKSPACE}", "sonar-project.properties"
            }
        }
		
        stage('SonarQube Quality Gate') {
		    // we don't want to tie up an agent for this
		    agent none
			
		    steps {
				waitForSonarQubeAnalysis 60, ['OK','WARN']
			}
        }
	
		stage('Approve for QA') {
            steps {
            //    input 'Sally forth?'
			    echo 'Pipeline done'
            }
		}
    }
}