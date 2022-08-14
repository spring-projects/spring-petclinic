pipeline {
    agent { label 'built-in' }
    options { 
        timeout(time: 1, unit: 'HOURS')
    }
    
    stages {
        stage('Source Code') {
            steps {
                git url: 'https://github.com/nikhatsultana639/spring-petclinic.git', 
                branch: 'main'
            }

        }
        stage('Artifactory-Configuration') {
            steps {
                rtMavenDeployer (
                    id: 'spc-deployer',
                    serverId: 'jfrogArtifact',
                    releaseRepo: 'nikhat-libs-release-local',
                    snapshotRepo: 'nikhat-libs-snapshot-local',

                )
            }
        }
        stage('Build the Code and sonarqube-analysis') {
            steps {

                rtMavenRun (
                    // Tool name from Jenkins configuration.
                    tool: 'apache-maven-3.8.5',
                    pom: 'pom.xml',
                    goals: 'clean install',
                    // Maven options.
                    deployerId: 'spc-deployer',
                )
      rtPublishBuildInfo serverId: 'jfrogArtifact'

            }
        }
        stage('reporting') {
            steps {
                junit testResults: 'target/surefire-reports/*.xml'
            }
        }


    }

}
