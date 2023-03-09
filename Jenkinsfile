pipeline {
     tools {
            maven 'MAVEN'
            jdk 'JDK-17'
        }
    agent { label 'Master'}
    triggers { pollSCM ('* * * * *') }
    stages{
        stage('vcs') {
            steps {
                git branch: 'declarative',
                    url: 'https://github.com/Bharatkumar5690/spring-petclinic.git'
            }
        }
        stage ('Artifactory configuration') {
            steps {
                rtServer (
                    id: "ARTIFACTORY_SERVER",
                    url: 'https://sbharatkumar.jfrog.io/artifactory',
                    credentialsId: 'JFROG_CLOUD_ADMIN'
                )

                rtMavenDeployer (
                    id: "MAVEN_DEPLOYER",
                    serverId: "ARTIFACTORY_SERVER",
                    releaseRepo: 'libs-release',
                    snapshotRepo: 'libs-snapshot'
                )

                rtMavenResolver (
                    id: "MAVEN_RESOLVER",
                    serverId: "ARTIFACTORY_SERVER",
                    releaseRepo: 'libs-release',
                    snapshotRepo: 'libs-snapshot'
                )
            }
        }
        stage ('package') {
            steps {
                rtMavenRun (
                    tool: 'MAVEN_DEFAULT',
                    pom: 'pom.xml',
                    goals: 'clean install',
                    deployerId: "MAVEN_DEPLOYER",
                )
                rtPublishBuildInfo (
                    serverId: "ARTIFACTORY_SERVER"
                )
            }
        }
        stage('Test the code by using sonarqube') {
            steps {
                withSonarQubeEnv('SONAR_CLOUD') {
                    sh 'mvn clean verify sonar:sonar -Dsonar.login=ea06c1ce5d1ee81e35db29d8cb0de69b42c70278 -Dsonar.organization=springpetclinic-1 -Dsonar.projectKey=springpetclinic-1_bha'
                }
            }
        }
        stage('Gathering the artifacts & test results') {
            steps {
                archiveArtifacts artifacts: '**/target/*.jar',
                                    onlyIfSuccessful: true,
                                    allowEmptyArchive: true
                junit testResults: '**/surefire-reports/TEST-*.xml'
            }
        }
    }
}
