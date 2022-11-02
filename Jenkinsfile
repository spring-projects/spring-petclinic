pipeline {
    agent {label 'java-11'}
    stages{
        stage ('git') {
            steps {
                git branch: 'main',
                url: 'https://github.com/Srikanthreddy1000/spring-petclinic.git'
            }
        stage('jfrog deployment') {
            steps {
                rtMavenDeployer (
                    id: "jfrog-artifact",
                    serverId: "srikanthjfrog",
                    releaseRepo: default-libs-release-local,
                    snapshotRepo: default-libs-snapshot-local
                )
            }
        }
        stage ('Maven') {
            steps {
                rtMavenRun (
                    tool: maven-build
                    pom: 'pom.xml',
                    goals: 'clean install',
                    deployerId: "srikanthjfrog"
                )
            }
        }
        stage ('Publish build info') {
            steps {
                rtPublishBuildInfo (
                    serverId: "srikanthjfrog"
                )
            }
            
        }
    }
 }
}