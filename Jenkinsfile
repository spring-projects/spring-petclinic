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
                echo "Running ${env.BUILD_ID} on ${env.JENKINS_URL}"
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
