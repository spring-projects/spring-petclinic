node {
 stage('SCM Checkout'){
     git branch: 'master', 

	   credentialsId: 'github', 

	   url: 'https://github.com/varunak12/spring-petclinic'
     }
     stage "compile package"{
     sh 'mvn package'
     }
  }
