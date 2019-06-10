node {


    stage('Checkout') {
        git 'https://github.com/ThilakrajKM/spring-petclinic'
    }
	
	stage('Build'){
	
		def mvn_version = 'M3'
		withEnv( ["PATH+MAVEN=${tool mvn_version}/bin"] ) {
			sh "mvn clean package"
		}
	}

    stage('Archive') {
        junit allowEmptyResults: true, testResults: '**/target/**/TEST*.xml'
    }

}