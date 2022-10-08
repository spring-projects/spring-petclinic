pipeline{
    agent{'JDK-11-MVN'}
    stages{
        stage('source code management'){
            steps{
                git branch: 'scripted', url: 'https://github.com/ShaikNasee/spring-petclinic.git'
            }
        }
        stage('build the code'){
            steps{
                sh '/usr/local/apache-maven-3.8.6/bin/mvn clean package'
            }
        }
    }
}