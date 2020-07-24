node('centos_host'){
    stage('scm'){
	git 'https://github.com/ametgud4u/spring-petclinic.git'
    }

    stage('build'){
	sh label: '', script: 'mvn clean package'
    }

    stage('Sonar') {
        withSonarQubeEnv('sonar') {
        sh 'mvn org.sonarsource.scanner.maven:sonar-maven-plugin:3.6.0.1398:sonar'
    }

    stage('postbuild'){
	junit '**/target/surefire-reports/*.xml'
	archiveArtifacts 'target/*.jar'
           }
            
    stage('Create Docker Image ') {
        sh label: '', script: 'BuildImage'
        }
    }
}
