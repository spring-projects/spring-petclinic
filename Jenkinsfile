pipeline{
agent { label 'node1' }
// triggers { pollSCM ('H * * * 1-5') }
parameters {
    choice (name: 'BRANCH_TO_BUILD', choices: ['main', 'Dev', 'Test'], description: 'Branch to build')
    string (name: 'MAVEN_GOAL', defaultValue: 'clean package', description: 'maven goal')
}
stages {
     stage('scm') {
        steps {
               git url: "https://github.com/nagarjuna33/spring-petclinicnew.git", 
                     branch:"main"
               }
     }

        stage ('SONAR QUBE SCAN') {
            steps{
                withSonarQubeEnv('SONAR_SCAN') {
                    sh 'mvn clean package sonar:sonar'
            }
            }
        }
        stage('Quality Gate') {
            steps {
                timeout(time: 20, unit: 'MINUTES'){
                    waitForQualityGate abortPipeline: true
                }
            }
        }
        stage('upload package to the jfrog ') {
            steps {
                rtUpload (
                serverId: 'JFROG_ID',
                spec: '''{
                    "files": [
                    {
                        "pattern": "./target/*.jar",
                        "target": "libs-release-local/"
                    }
                    ]
        }''',
        buildName: "$env.JOB_NAME",
        buildNumber: "$env.BUILD_NUMBER",
        project: 'springpet'
        )
        }
        }
        stage ('Publish build info') {
            steps {
                rtPublishBuildInfo (
                    serverId: "JFROG_ID"
                )
            }
        }
}
}
    
