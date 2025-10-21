pipeline {
    // 1. AGENT & TOOLS
    // Define the execution environment
    agent any

    // Define the tools to be automatically installed
    // This name MUST match the one you configured in Jenkins > Tools
    tools {
        maven 'Maven-3.9.11'
    }

    // 2. STAGES (The CI Workflow)
    // The main body of our pipeline
    stages {
        // The 'Build' stage runs compile and tests, then packages the app
        stage('Build, Test & Package') {
            steps {
                echo 'Building the Spring PetClinic application...'
                
                // 3. RUNNING BUILD TOOLS
                // Run the Maven 'package' goal.
                // This automatically compiles, runs tests, and creates the .jar file.
                sh 'mvn clean package'
            }
        }

        // This stage runs *after* the build to collect the results
        stage('Archive & Publish Reports') {
            steps {
                echo 'Archiving the deployable JAR file...'
                
                // 4. ARCHIVING ARTIFACTS
                // Find any .jar file in the 'target' directory and save it
                // 'fingerprint: true' lets Jenkins track where this file is used
                archiveArtifacts artifacts: 'target/*.jar', fingerprint: true

                echo 'Publishing JUnit test results...'
                
                // 5. TEST REPORTING
                // Find the XML test reports and let Jenkins parse them
                // This is what gives you the "Test Result" graphs
                junit 'target/surefire-reports/*.xml'
            }
        }
    }

    // 6. POST-BUILD ACTIONS
    // This block runs after all stages are complete
    post {
        // 'success' runs only if the pipeline is green
        success {
            echo 'Build Succeeded! Ready to deploy.'
        }
        
        // 'failure' runs only if the pipeline is red
        failure {
            echo 'Build Failed. Please review the logs.'
            // You could add an email notification here
        }
        
        // 'always' runs regardless of success or failure
        always {
            echo 'Pipeline run finished.'
            // This is a great place for cleanup steps
        }
    }
}
