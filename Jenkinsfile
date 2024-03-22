pipeline{
    agent any
    tools{
        maven "MVN_DEFAULT"
    }
    stages {
        stage('vcs')
        {
            steps{
                git url:'https://github.com/spring-projects/spring-petclinic.git',
                branch: 'main'
            }
        }
        stage('exce mvn') {
            steps{
                MAVENRUN(
                    tool: 'MVN_DEFAULT',
                    pom:'pom.xml',
                    goal:'clean install',
                    deployerID:'MVN_DEPLOYER'
                )
            }
        }
        stage('deploy mvn')
        {
            steps{
                MAVENDEPLOY(
                    id: 'spc-deployer',
                    serverId: 'JFROG_INSTANCE_ID',
                     releaseRepo: 'srini415-libs-release-local',
                    snapshotRepo: 'srini415-libs-snapshot-local'
                )
            }
        }
         stage('reporting') {
            steps {
                junit testResults: 'target/surefire-reports/*.xml'
            }
        }
    }

}