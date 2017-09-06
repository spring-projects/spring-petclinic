pipeline {
    //environment {
    //}
	
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
		
        stage('SonarQube analysis') {
			steps {
				script {
	                scannerHome = tool 'SonarQube_Scanner_3.0.3.778';
				}

                withSonarQubeEnv('Staging') {
				    echo "${scannerHome}"
                    sh "${scannerHome}/bin/sonar-scanner"
                }
            }
        }
		
        stage('SonarQube Quality Gate') {
		    steps {
                timeout(time: 1, unit: 'HOURS') { 
				    script {
					    // these are the statuses that we'll allow
					    def alowableStatuses = ['OK','WARN']
						
						// we need to wait for the quality check to complete
                        def qg = waitForQualityGate() 
						
						// if the status we got back, isn't one of the logal ones, then
						// we need o fail the build
                        if (!allowableStatuses.contains(qg.status)) {
                            error "Pipeline aborted due to quality gate failure: ${qg.status}"
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