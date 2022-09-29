node('OPENJDK-11-MAVEN') 
{
    stage('vcs') 
    {
		git branch: 'REL_INT_1.0', url: 'https://github.com/subramanyamgb/spring-petclinic.git'
    }
    stage("build")
    {
        sh '/opt/apache-maven-3.8.6/bin/mvn package'
    }
    stage("archive results")
    {
        junit '**/surefire-reports/*.xml'
    }
 }