pipeline {
    agent {
        label 'dockerbuild'
    }

    /////////////////////////////////////////////////////////////////////
    // START :
    // Definition of Jenkins job configuration
    /////////////////////////////////////////////////////////////////////
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
        stage ('print job summary') {
            steps {
                println "----Git repo link: https://github.com/mnpatel0611/spring-petclinic"
                println "------------------------------------------------"
                println "--------------------INFO------------------------"
                println "Docker image pushed to Dockerhub: mpatel011/spring-petclinic:$BUILD_NUMBER"
                println "artifactory_url: $artifactory_url"
                println "artifactory_repo: $artifactory_repo"
                println "------------------------------------------------"
                println "------------------------------------------------"
            }
        }
    }
}
