node('jdk11-mvn3.6.3') {
    properties([pipelineTriggers([cron(' 0 */1 * * * ')])])
    stage('git') 
        git 'https://github.com/bhargavi-vaduguri/spring-petclinic.git'
    }
    stage('Build') { 
        sh '''
            echo "PATH=${PATH}"
            echo "M2_HOME=${M2_HOME}"
            
        '''
        sh '/usr/local/apache-maven-3.6.3/bin/mvn clean package'
    }
    stage('archive') {
        archive 'target/*.jar'
    }
