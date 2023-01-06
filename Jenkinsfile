pipeline {
    agent { label 'jenkins123' }
    triggers { pollSCM(* * * * *) }
    stages {
        stage('git') {
            steps {
                git branch: "main",
                url: "https://github.com/Srikanthreddy1000/spring-petclinic.git"
            }
        }
        stage('Mvn Build') {
            steps {
                sh "mvn package"
            }
        }
        stage('sonarqube analysis') {
            steps {
              withSonarQubeEnv('My SonarQube Server') {
                sh 'mvn clean package sonar:sonar'
              }
            }
        }
        stage('qualitygate') {
            steps {
              timeout(time: 1, unit: 'HOURS') {
                waitForQualityGate abortPipeline: true
              }
            }
        }
        stage('Jfrog') {
            steps {
              rtMavenDeployer (
                    id: "Jfrog-id-deployer",
                    serverId: "srikanthjfrog",
                    releaseRepo: libs-release-local,
                    snapshotRepo: libs-snapshot-local
                )
              rtMavenResolver (
                    id: "Jfrog-id-resolver",
                    serverId: "srikanthjfrog",
                    releaseRepo: libs-release-local,
                    snapshotRepo: libs-snapshot-local
              )  
            }

          }
        }
    }
}