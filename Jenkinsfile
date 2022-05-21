node('jdk11-mvn3.8.4') {
    stage('git') 
        git 'https://github.com/bhargavi-vaduguri/spring-petclinic.git'
    }
    stage('Build') { 
        sh 'mvn clean package'
    }
    stage('archive') {
        archive 'target/*.jar'
    }
