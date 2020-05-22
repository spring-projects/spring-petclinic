

node('jfrognew'){
    stage('scm'){
	git 'https://github.com/ametgud4u/Devops.git'
    }

    stage('build'){
	sh label: '', script: 'mvn clean package'
    }

    stage('postbuild'){
	junit '**/target/surefire-reports/*.xml'
	archiveArtifacts 'target/*.jar'
    }

}