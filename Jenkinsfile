node {

	tools{
        maven "Maven-3.3.9"
        jdk "JAVA_HOME"
		
    }
    stage('Checkout') {
        git 'https://github.com/ThilakrajKM/spring-petclinic'
		
    }
	
	stage('Build'){
		sh "mvn clean package"
		
	}

    stage('Archive') {
        junit allowEmptyResults: true, testResults: '**/target/**/TEST*.xml'
		
    }

}