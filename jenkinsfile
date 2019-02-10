node {
    stage('SCM'){
        git 'https://github.com/pusa9/spring-petclinic.git'

    }
    stage ('build the packages'){
sh 'mvn package'
    }

     stage ('archival'){
         
archive 'target/*.jar'
    }
 
}