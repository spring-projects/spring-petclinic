pipeline {
    agent {
        label 'dockerbuild'
    }

    /////////////////////////////////////////////////////////////////////
    // START :
    // Definition of Jenkins job configuration
    // Job Parameters and any other config is set here, not in Jenkins UI
    /////////////////////////////////////////////////////////////////////
    options {
        disableConcurrentBuilds()//Do NOT run in parallel!
        buildDiscarder(logRotator(numToKeepStr: '50'))
        timeout(time: 120, unit: 'MINUTES')
        skipStagesAfterUnstable()
        timestamps()
    }
    environment {
        artifactory_url="https://petclinic.jfrog.io/artifactory"
        artifactory_repo="spring-petclinic"
    }
    /////////////////////////////////////////////////////////////////////
    // END
    /////////////////////////////////////////////////////////////////////

    /////////////////////////////////////////////////////////////////////
    // START : Stages
    /////////////////////////////////////////////////////////////////////
    stages {
        stage('build image') {
            steps {
                script {
                    dockerImage = docker.build "mpatel011/spring-petclinic:$BUILD_NUMBER"
                }
            }
        }
        stage('deploy image') {
            steps {
                script {
                    docker.withRegistry('' , 'dockerhub') {
                        dockerImage.push()
                    }
                }
            }
        }
    }
}
