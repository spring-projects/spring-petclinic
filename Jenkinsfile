node {

	agent any
	
	tools{
        maven "Maven-3.3.9"
        jdk "JAVA_HOME"
		
    }
    stage('Checkout') {
        git 'https://github.com/ThilakrajKM/spring-petclinic'
		
    }
	
	stage('Build') {
		  // Run the maven build
		  withEnv(["MVN_HOME=$mvnHome"]) {
				 if (isUnix()) {
					sh '"$MVN_HOME/bin/mvn" -Dmaven.test.failure.ignore clean package'
				 } else {
					bat(/"%MVN_HOME%\bin\mvn" -Dmaven.test.failure.ignore clean package/)
				 }
		  }
    }

    stage('Archive') {
        junit allowEmptyResults: true, testResults: '**/target/**/TEST*.xml'
		
    }

}