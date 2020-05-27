

node('jfrognew'){
    stage('scm'){
	git 'https://github.com/ametgud4u/spring-petclinic.git'
    }

    stage('build'){
	sh label: '', script: 'mvn clean package'
    }

    stage('postbuild'){
	junit '**/target/surefire-reports/*.xml'
	archiveArtifacts 'target/*.jar'
    }

    stage('Sonar') {
        withSonarQubeEnv('SONAR') {
        sh 'mvn org.sonarsource.scanner.maven:sonar-maven-plugin:3.6.0.1398:sonar'
        }
    }
}

