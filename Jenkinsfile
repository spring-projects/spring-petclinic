pipeline {
    agent any
    options {
        timeout(time: 30, unit: 'MINUTES') 
    }
    triggers {
        pollSCM('* * * * *')
    }
    tools {
        maven 'MAVEN_3.8'
        jdk 'JDK_17' 
    }
    stages {
        stage ('git'){
            git branch: 'develop',
                url: 'https://github.com/Akhil-Tejas225/spring-petclinic.git'
        }
        stage ('build and package') {
            rtMavenDeployer (
                id: 'SPC_DEPLOYER',
                serverId: 'JFROG_CLOUD',
                releaseRepo: 'atdevops-libs-snapshot',
                snapshotRepo: 'atdevops-libs-snapshot'
            )
            rtMavenRun (
                tool: 'Maven_3.8',
                deployerId: 'SPC_DEPLOYER',
                pom: 'pom.xml',
                goals: 'clean install'
            )
            rtPublishBuildInfo (
                serverId: 'JFROG_CLOUD'
            )
        stage ('reporting'){
            junit testResults: '**/target/surefire-reports/TEST-*.xml'
        }

        }
        }
    }
  