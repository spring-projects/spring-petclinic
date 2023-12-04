pipeline {
    agent any
    environment {
            DOCKERHUB_CREDENTIALS = credentials('mihaivalentingeorgescu-dockerhub')
    }
    stages {
        stage('Checkstyle') {
            steps {
                sh 'export JAVA_HOME=/usr/lib/jvm/java-17-openjdk-amd64'
                sh 'mvn checkstyle:checkstyle'
                archiveArtifacts artifacts: 'checkstyle-result.html', onlyIfSuccessful: true
            }
        }
        stage('Tests') {
            steps {
                echo " now we will begin the tests "
                sh 'mvn test'
            }
        }
        stage('Build') {
            steps {
                echo "now we will begin the build"
                script {
                    def appBuildoutPut = sh (script: 'mvn clean package -Dskiptests', returnStatus: true)
                    if (appBuildoutPut == 0) {
                        echo "build SUCCESSFUL"
                    } else {
                        error "Build failed"
                    }
                }
            }
        }
        stage('Create docker image') {
            steps {
                echo "now we will begin the creation of the docker image"
                script {
                    def dockerBuildOutput = sh(script: 'docker build -t imagine_spring_petclinic:0.1 .', returnStatus: true)
                    if (dockerBuildOutput == 0) {
                        echo "Docker image creation successful"
                    } else {
                        error "Docker image creation FAILED" // Optional: Terminate the pipeline with an error message if the build fails
                    }
                }
            }
        }
        stage('Tag the docker image') {
            when {
                // Condition to execute the stage when the branch is main
                    changeRequest()
                }
            steps {
                echo "now we will tag the docker image "
                script {
                    def imageTag = sh(script: 'docker tag imagine_spring_petclinic:0.1 mihaivalentingeorgescu/mr:0.1', returnStatus: true)
                    if (imageTag == 0) {
                        echo "Image tagged successfully"
                    } else {
                        error "Image tagging FAILED" 
                    }
                }
            }
        }
        stage('Login to Dockerhub') {
            steps {
                echo "now we will login to dockerhub"
                script {
                    def loginDocker = sh(script: 'echo $DOCKERHUB_CREDENTIALS_PSW | docker login -u $DOCKERHUB_CREDENTIALS_USR --password-stdin', returnStatus: true)
                    if (loginDocker == 0) {
                        echo "Login ended successfully"
                    } else {
                        error "Login FAILED" 
                    }
                }
            }
        }
        stage('Push to DockerHub') {
            when {
                // Condition to execute the stage when the branch is main
                    changeRequest()
                }
            steps {
                echo "now we will push to the docker file"
                script {
                    def pushToDocker = sh(script: 'docker push mihaivalentingeorgescu/mr:0.1', returnStatus: true)
                    if (pushToDocker == 0) {
                        echo "Push made successfully"
                    } else {
                        error "Docker push FAILED" 
                    }
                }
            }
        }
        stage('Change stages') {
            steps {
                echo "this is the start of the new stage with the main branch"
            }
        }
        stage('Tag docker image again for the main repo') {
            when {
                // Condition to execute the stage when the branch is 'main'
                expression {
                    return env.BRANCH_NAME == 'main'
                }
            }
            steps {
                echo "now we will tag the docker image for the main branch"
                script {
                    def tagDockerImage = sh(script: 'docker tag imagine_spring_petclinic:0.1 mihaivalentingeorgescu/main:0.1', returnStatus: true)
                    if (tagDockerImage == 0) {
                        echo "Docke tag ended successfully"
                    } else {
                        error "Docker tag FAILED" 
                    }
                }
            }
        }
        stage('Push docker image to main repository') {
            when {
                // Condition to execute the stage when the branch is 'main'
                expression {
                    return env.BRANCH_NAME == 'main'
                }
            }
            steps {
                echo "now we will push the image to the docker main repository"
                script {
                    def pushDockerImageToMain = sh(script: 'docker push mihaivalentingeorgescu/main:0.1', returnStatus: true)
                    if (pushDockerImageToMain == 0) {
                        echo "Docke tag ended successfully"
                    } else {
                        error "Docker tag FAILED" 
                    }
                }
            }
        }
    }
}
