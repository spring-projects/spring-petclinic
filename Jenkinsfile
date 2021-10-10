pipeline {
    agent any
    stages {
        stage('Build') {
            steps {
                // Get some code from a GitHub repository
                git branch: 'main', url: 'https://github.com/FilipPucoski/petcllinic'
                bat './mvnw compile'
            }
        }
        stage ('Test') {
            steps {
                bat './mvnw test'
            }
        }
        stage ('Archive Artifacts') {
            steps {
                bat './mvnw package'
            }
             post {
                // If Maven was able to run the tests, even if some of the test
                // failed, record the test results and archive the jar file.
                success {
                    junit '**/target/surefire-reports/*.xml'
                    archiveArtifacts 'target/*.jar'
                }
            }
        }
        stage ('Publish package to Octopus') {
            steps {
                octopusPushPackage (
                overwriteMode: 'FailIfExists', 
                packagePaths: 'target/spring-petclinic-2.5.0-SNAPSHOT.jar', 
                serverId: 'Octopus Deploy', 
                spaceId: '', 
                toolId: 'Default')
              octopusPushBuildInformation (
                    toolId: 'Default', 
                    serverId: 'Octopus Deploy', 
                    spaceId: '', 
                    commentParser: 'GitHub', 
                    overwriteMode: 'FailIfExists',
                    packageId: 'spring-petclinic-2.5.0-SNAPSHOT.jar', 
                    packageVersion: '0.1.${BUILD_NUMBER}', 
                    verboseLogging: false, 
                    additionalArgs: '--debug', 
                    gitUrl: 'https://github.com/FilipPucoski/petcllinic'
		    ) 
            }
        }
    }
}