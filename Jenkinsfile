node {

	stage('Configure') {
        env.PATH = "${tool 'maven-3.3.9'}/bin:${env.PATH}"
	}

    stage('Checkout') {
        git 'https://github.com/ThilakrajKM/spring-petclinic'
    }

    stage('Build') {
        sh 'mvn clean package'
    }

    stage('Archive') {
        junit allowEmptyResults: true, testResults: '**/target/**/TEST*.xml'
    }

}