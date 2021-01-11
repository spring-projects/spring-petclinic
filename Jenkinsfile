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
        artifactory_repo="${artifactory_url}/spring-petclinic"
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
                echo "build <<< ${BUILD_NUMBER} >>> starting..."
            }
        }
        stage('build and push to artifactory') {
            steps {
                sh "mvn clean validate compile test package"
                sh "ls -la target"
                echo "------------------------------------------------"
                echo "--------------------Start - Push to JFROG Artifactory------------------------"
                sh 'curl -u jfroguser:AdminPassword1 -T ./target/spring-petclinic-2.4.0.BUILD-SNAPSHOT.jar "${artifactory_repo}/spring-petclinic-2.4.0.BUILD-${BUILD_NUMBER}.jar"'
                echo "--------------------Complete - Push to JFROG Artifactory------------------------"
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
                echo "Artifact URL: ${artifactory_repo}/spring-petclinic-2.4.0.BUILD-${BUILD_NUMBER}.jar"
                echo "Docker image pushed to Dockerhub: mpatel011/spring-petclinic:$BUILD_NUMBER"
                echo "------------------------------------------------"
                echo "------------------------------------------------"
            }
        }
    }


}
