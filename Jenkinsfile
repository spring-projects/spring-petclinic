pipeline {
    agent any
 stages{
   stage('git clone'){
     steps{
       git url:'https://github.com/praveenkumar57/spring-petclinic.git'
      }
    }
   stage('build activities'){
     steps{
      sh ''' 
      mvn clean package
      '''
   }
}
 
}

}
