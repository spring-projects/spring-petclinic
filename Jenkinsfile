pipeline {
    //environment {
    //}
	
	//tools {
	//    maven 'Maven 3.5.0'
	//}

    agent none

    stages {
        stage('Build, Test, and Package') {
            agent any
            steps {
                sh "mvn clean package"
            }
        }
		
        stage('SonarQube analysis') {
		    agent any
			steps {
			    // jenkins SQ token: 9ba8e3134a1cbc76910a73579a888b5e91df9717
                //withSonarQubeEnv('Staging') { 
                //    sh 'mvn org.sonarsource.scanner.maven:sonar-maven-plugin:3.3.0.603:sonar ' + 
                //       '-f pom.xml ' +
                //       '-Dsonar.projectKey=com.huettermann:all:master ' +
                //       '-Dsonar.login=$SONAR_UN ' +
                //       '-Dsonar.password=$SONAR_PW ' +
                //       '-Dsonar.language=java ' +
                //       '-Dsonar.sources=. ' +
                //       '-Dsonar.tests=. ' +
                //       '-Dsonar.test.inclusions=**/*Test*/** ' +
                //       '-Dsonar.exclusions=**/*Test*/**'
                //}

				script {
				    def scannerHome = tool 'SonarQube Scanner 3.0.3.778';
				}
                withSonarQubeEnv('Staging') {
                    sh "${scannerHome}/bin/sonar-scanner"
                }
            }
        }
		
		/*
        stage("SonarQube Quality Gate") {
		    steps {
                timeout(time: 1, unit: 'HOURS') { 
                    def qg = waitForQualityGate() 
                    if (qg.status != 'OK') {
                        error "Pipeline aborted due to quality gate failure: ${qg.status}"
                    }
                }
			}
        }
		*/
	
		stage('Approve for QA') {
            agent any
            steps {
                input 'Sally forth?'
            }
		}
    }
}