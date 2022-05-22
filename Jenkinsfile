pipeline {
    agent { label 'JDK11' }
    options { 
        timeout(time: 1, unit: 'HOURS')
    }
    triggers {
        cron('0 * * * *')
    }
    stages {
        stage('Source Code') {
            steps {
                git url: 'https://github.com/GitPracticeRepo/spring-petclinic.git', 
                branch: 'main'
            }
            
        }
        stage('Artifactory-Configuration') {
            steps {
                rtMavenDeployer (
                    id: 'spc-deployer',
                    serverId: 'JFROG_INSTANCE',
                    releaseRepo: 'qtecomm-libs-release-local',
                    snapshotRepo: 'qtecomm-libs-snapshot-local',
                    
                )
            }
        }
        stage('Build the Code and sonarqube-analysis') {
            steps {
                // withSonarQubeEnv('SONAR_LATEST') {
                //     sh script: "mvn ${params.GOAL} sonar:sonar"
                // }

                rtMavenRun (
                    // Tool name from Jenkins configuration.
                    tool: 'MVN_DEFAULT',
                    pom: 'pom.xml',
                    goals: 'clean install',
                    // Maven options.
                    deployerId: 'spc-deployer',
                )
                
                // stash name: 'spc-build-jar', includes: 'target/*.jar'
            }
        }
        stage('reporting') {
            steps {
                junit testResults: 'target/surefire-reports/*.xml'
            }
        }
        // stage("Quality Gate") {
        //     steps {
        //       timeout(time: 1, unit: 'HOURS') {
        //         waitForQualityGate abortPipeline: true
        //       }
        //     }
        //   }
        
    }
    // post {
    //     success {
    //         // send the success email
    //         echo "Success"
    //         mail bcc: '', body: "BUILD URL: ${BUILD_URL} TEST RESULTS ${RUN_TESTS_DISPLAY_URL} ", cc: '', from: 'devops@qtdevops.com', replyTo: '', 
    //             subject: "${JOB_BASE_NAME}: Build ${BUILD_ID} Succeded", to: 'qtdevops@gmail.com'
    //     }
    //     unsuccessful {
    //         //send the unsuccess email
    //         mail bcc: '', body: "BUILD URL: ${BUILD_URL} TEST RESULTS ${RUN_TESTS_DISPLAY_URL} ", cc: '', from: 'devops@qtdevops.com', replyTo: '', 
    //             subject: "${JOB_BASE_NAME}: Build ${BUILD_ID} Failed", to: 'qtdevops@gmail.com'
    //     }
    // }
}