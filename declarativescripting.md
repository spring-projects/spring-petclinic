pipeline script :
 pipeline{   
    agent {label 'spring'}
     stages {
         stage('spring1') {
         steps{
         git url:'https://github.com/spring-petclinic spring-framework-petclinic.git',
          branch: 'master'
        }
        }
 stage('bulid'){
 steps{
   sh 'mvn package'  
    }
    }
}
}