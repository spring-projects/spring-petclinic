pipeline {
     agent {label 'MASTER'}
	 triggers {
	    upstream(upstreamProjects: 'dummy', threshold: hudson.model.Result.SUCCESS)
	 } 
     stages {
	stage('source') {
             steps {
               git 'https://github.com/seshi7/spring-petclinic.git'
             }
	}
	stage('Package') {
	    steps {
              sh 'mvn package' 
              input 'Ready to go?'
              archiveArtifacts 'target/*.jar'
	    }
	}
     }
}
