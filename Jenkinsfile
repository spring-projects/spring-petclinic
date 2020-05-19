pipeline {
   agent {label 'MASTER'}
   stages {
      stage('scm'){
	    steps {
		    git 'https://github.com/seshi7/spring-petclinic.git'
     	}
	  }
	  stage ('Package'){
	     steps {
		     sh 'mvn package'
		 }
	  }
   }
}
