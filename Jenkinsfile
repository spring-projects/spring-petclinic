node {
  
   stage('SCM') {
      // git clone
	  git 'https://github.com/aja6009/spring-petclinic.git'
	  
   }
   
   stage ('build package') {
      // mvn package
	  sh 'mvn package'
   }
   
   stage ('archival') {
     // archival artifacts
	 archive 'target/*.jar'
   }
   
} 