def call(){
    pipeline {
    agent  { label 'node' }
    parameters { choice(name: 'CHOICES', choices: ['main', 'new_branch', 'spring_master'], description: 'using parameters') }
    triggers { pollSCM('* * * * *') }
    stages {
        stage('git') {
            steps {
                git branch: "${params.CHOICES}", 
                url: 'https://github.com/gopivurata/spring-petclinic.git'
            }

        }
        stage('JFROG configuration') {
            steps {
                rtMavenDeployer (
                    id: 'MAVEN_DEPLOYER',
                    serverId: 'JFROG_ID',
                    releaseRepo: 'jfrog-libs-release',
                    snapshotRepo: 'jfrog-libs-snapshot'
                )
            }
        }
        stage('maven build') {
            steps {
                rtMavenRun (
                    tool: 'MVN', // Tool name from Jenkins configuration
                    pom: 'pom.xml',
                    goals: 'clean install',
                    deployerId: "MAVEN_DEPLOYER"
                    
                )
            }
        }
         stage('SonarQube') {
            steps {
                withSonarQubeEnv('SONAR_Q') {
                    sh script: 'mvn clean package sonar:sonar'
                }
            }
        }
        stage ('publish build info') {
            steps {
                rtPublishBuildInfo (
                    serverId: "JFROG_ID"
                )
            }
        }
    }

}
}
