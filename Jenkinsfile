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
			    /*
				script {
	                scannerHome = tool 'SonarQube_Scanner_3.0.3.778';
				}

                withSonarQubeEnv('Staging') {
				    echo "${scannerHome}"
                    sh "${scannerHome}/bin/sonar-scanner"
                }
				*/
				//startSonarQubeAnalysis "SonarQube_Scanner_3.0.3.778", "Staging", "${env.WORKSPACE}/sonar-project.properties"
				startSonarQubeAnalysis {
				    scanner = "SonarQube_Scanner_3.0.3.778"
					server  = "Staging"
					pathToProjectSonarSettings = "${env.WORKSPACE}/sonar-project.properties"
				}
            }
        }
		
        stage('SonarQube Quality Gate') {
		    steps {
                timeout(time: 1, unit: 'HOURS') { 
				    script {
					    // these are the statuses that we'll allow
					    def allowableQualityGateStatuses = ['OK', 'WARN']
						
						// we need to wait for the quality check to complete
                        def qualityGate = waitForQualityGate() 
						
						// if the status we got back, isn't one of the logal ones, then
						// we need to fail the build
                        if (!allowableQualityGateStatuses.contains(qualityGate.status)) {
                            error "Pipeline aborted due to quality gate failure: ${qualityGate.status}"
                        }
                    }
                }
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