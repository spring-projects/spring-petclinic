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
                        def qg = waitForQualityGate() 
                        if (qg.status != 'OK') {
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