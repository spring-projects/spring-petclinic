pipeline {
    agent  { label 'node' }
    parameters {
        string(name: 'branch', defaultValue: 'gopi', description: 'job build on default branch master')
       // choice(name: 'CHOICES', choices: ['main', 'new_branch', 'spring_master','gopi' ], description: 'using parameters')
    }
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
                    serverId: 'MY_JFROG',
                    releaseRepo: 'java-maven-libs-release',
                    snapshotRepo: 'java-maven-libs-snapshot'
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
                withSonarQubeEnv('MY_SONARQ') {
                    sh script: 'mvn clean package sonar:sonar'
                }
            }
        }
       // stage("Quality Gate") {
        //    steps {
         //     timeout(time: 1, unit: 'HOURS') {
         //       waitForQualityGate abortPipeline: false
         //     }
         //   }
         // }
        stage ('publish build info') {
            steps {
                rtPublishBuildInfo (
                    serverId: "MY_JFROG"
                )
            }
        }
    }
    post {
        always {
            echo 'completed job'
            mail subject: "Build completed for Jenkins JOB $env.JOB_NAME",
                 body: "Build completed for Jenkins JOB $env.JOB_NAME \n Click Here: $env.JOB_URL",
                 to: 'gopivurata1992@gmail.com'
        }
        failure {
            mail subject: "Build Faild for Jenkins JOB $env.JOB_NAME with Build ID $env.BUILD_ID",
                 body: "Build Failed for Jenkins JOB $env.JOB_NAME",
                 to: 'gopivurata1992@gmail.com'

        }
        success {
            junit '**/surefire-reports/*.xml'
        }


    }

}
