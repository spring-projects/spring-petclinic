#!groovy
node('maven_ubuntu')
{
    stage('vcs')
    {
       git 'https://github.com/lakshmi164585/spring-petclinic.git'
    }
    stage('build') 
    {
        sh './gradlew'

    }
}
