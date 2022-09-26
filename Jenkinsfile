pipeline {
    agent { label 'JDK11-MVN'}
        stages{
            stage('git'){
                steps{
                    git url: 'https://github.com/ziyad-ansari/spring-petclinic.git' ,
                    branch: 'REL_V1'
                }
            }
            stage('build'){
                steps{
                    sh 'mvn package'
                    junit '**/surefire-reports/*.xml'
                }
            }
        }
}