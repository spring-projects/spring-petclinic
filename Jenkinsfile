pipeline {
    agent { label 'UBUNTU_NODE1' }
    triggers { pollSCM('* * * * *') }
    stages {
        stage('get url') {
            steps {
                git branch: 'sweety',
                    url: 'https://github.com/Madhuri-chinta/spring-petclinic.git'     
            }
        }    
        stage('package') {
            steps {
                sh './mvnw package'
            } 
        } 
        stage('sonarqube') {
            steps {
                withSonarQubeEnv('madhuri') {
                    sh 'mvn clean package sonar:sonar'
              }
            }
        }
        stage('artifactory') {
            steps {
                rtMavenDeployer (
                    id: "jfrog",
                    serverId: "madhuri",
                    releaseRepo: "madhuri",
                    snapshotRepo: "madhuri"
                )
            }
        }
        stage ('Exec Maven') {
            steps {
                rtMavenRun (
                    tool: "MAVEN_GOAL", // Tool name from Jenkins configuration
                    pom: 'pom.xml',
                    goals: 'clean install',
                    deployerId: "jfrog"
                )
            }
        }

        stage ('Publish build info') {
            steps {
                rtPublishBuildInfo (
                    serverId: "madhuri"
                )
            }
        }
        }          
    }    

    

    
