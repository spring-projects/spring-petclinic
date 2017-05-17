pipeline {
    agent none
    stages {
       stage('Build') {
           agent {
               docker {
                   image 'maven:3.5.0'
                   args '--network=plumbing_default'
               }
           }
           steps {
               configFileProvider(
                       [configFile(fileId: 'nexus', variable: 'MAVEN_SETTINGS')]) {
                   sh 'mvn -s $MAVEN_SETTINGS clean deploy -DskipTests=true -B'
               }
           }
       }
       stage('Deploy to Tomcat') {
           agent {
               docker {
                   image 'alpine'
               }
           }
           steps {
               sh 'cp target/petclinic.war /usr/share/jenkins/ref/petclinic/petclinic.war'
           }
       }
       stage('Sonar') {
           agent  {
               docker {
                   image 'sebp/sonar-runner'
                   args '--network=plumbing_default'
               }
           }
           steps {
               sh '/opt/sonar-runner-2.4/bin/sonar-runner'
           }
       }
        stage('Selenium') {
            agent {
                docker {
                    image 'liatrio/selenium-firefox'
                    args '--network=plumbing_default'
                }
            }
            steps {
                sh 'ruby petclinic_spec.rb'
            }
        }
    }
}
