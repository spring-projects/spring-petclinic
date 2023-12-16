    // node {
    //     stage('sourcecode'){
    //         //build step
    //         git branch: 'main', url: 'https://github.com/vishnu1411/spring-petclinic.git'
    //     }
    //     stage('Build and install'){
    //         //building using maven and install
    //         sh 'mvn clean install'
    //     }
    //     stage('Archive  test results'){
    //         //insteall & archive  test reuslts
    //         junit '**/surefire-reports/*xml'
    //         archiveArtifacts artifacts: '**/*.jar', followSymlinks: false
    //     }
    // }
pipeline{
    agent any

    stages {
        stage ('SourceCode') {
            steps {
                //source code from github
                git branch: 'main', url: 'https://github.com/vishnu1411/spring-petclinic.git'
            }

        }
        stage ('Build and install') {
            steps {
                //clean and build using maven
                sh 'mvn clean install'
            }

        }
        stage ('Archive test results') {
            steps {
                //Archive the test results
                junit '**/surefire-reports/*xml'
                archiveArtifacts artifacts: '**/*.jar', followSymlinks: false
            }

        }
    }
}