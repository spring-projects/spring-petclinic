pipeline {
    agent any
    parameters {
        string(name: 'MAVEN_GOAL', defaultValue: 'clean install', description: 'maven goal')

    }
    triggers { pollSCM('* * * * *') }
	
    stages {
        stage('vcs') {
            steps {
                git branch: "google", url: 'https://github.com/vikasvarmadunna/spring-petclinic.git'
            }

        }
         stage ('Artifactory configuration') {
            steps {
                rtServer (
                    id: "jfrog",
                    url: "https://hellohivikas.jfrog.io",
                    credentialsId: "defrog"
                )

                rtMavenDeployer (
                    id: "MAVEN_DEPLOYER",
                    serverId: "jfrog",
                    releaseRepo: 'success-libs-release-local',
                    snapshotRepo: 'success-libs-snapshot-local'
                )


            }
        }

        stage ('Exec Maven') {
            steps {
                rtMavenRun (
                    tool: 'mvn-3.6.3', // Tool name from Jenkins configuration
                    pom: 'pom.xml',
                    goals: 'clean install',
                    deployerId: "MAVEN_DEPLOYER"
                )
            }
        }

        stage ('Publish build info') {
            steps {
                rtPublishBuildInfo (
                    serverId: "jfrog"
                )
            }
        }


    }

}
