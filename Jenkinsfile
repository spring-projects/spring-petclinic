pipeline {
    agent {
        label 'dockerbuild'
    }

    /////////////////////////////////////////////////////////////////////
    // START :
    // Definition of Jenkins job configuration
    /////////////////////////////////////////////////////////////////////
    options {
        skipStagesAfterUnstable()
    }
    environment {
        artifactory_url="https://petclinic.jfrog.io/artifactory"
        artifactory_repo="spring-petclinic"
        build_id="$BUILD_NUMBER"
    }
    /////////////////////////////////////////////////////////////////////
    // END
    /////////////////////////////////////////////////////////////////////

    /////////////////////////////////////////////////////////////////////
    // START : Stages
    /////////////////////////////////////////////////////////////////////
    stages {
        stage('build started') {
            steps {
                echo "////////////////  build <<< ${env.BUILD_ID} >>> started  ////////////////////"
            }
        }
        stage('build maven package') {
            steps {
                sh "mvn validate compile test package"
            }
        }
        stage('build docker image') {
            steps {
                script {
                    dockerImage = docker.build "mpatel011/spring-petclinic:$BUILD_NUMBER"
                }

                sh "docker images"
            }
        }
        stage('push docker image') {
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
                echo "----Git repo link: https://github.com/mnpatel0611/spring-petclinic"
                echo "------------------------------------------------"
                echo "--------------------INFO------------------------"
                echo "Docker image pushed to Dockerhub: mpatel011/spring-petclinic:$BUILD_NUMBER"
                echo "artifactory_url: $artifactory_url"
                echo "artifactory_repo: $artifactory_repo"
                echo "------------------------------------------------"
                echo "------------------------------------------------"
            }
        }
    }
}
