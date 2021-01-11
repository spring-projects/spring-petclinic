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
        stage('build maven package') {
            steps {
//                 sh "java -version"
//                 sh "mvn -version"
//                 withMaven {
                sh "mvn clean validate compile test package"
                sh "ls -la target"
                sh 'curl -X -u jfroguser:AdminPassword1 -T ./target/spring-petclinic-2.4.0.BUILD-SNAPSHOT.jar "${artifactory_repo}/spring-petclinic-2.4.0.BUILD-${BUILD_NUMBER}.jar"'
//                 rtServer (id: 'jenkins-artifactory-server',url: 'https://petclinic.jfrog.io/artifactory',username: 'jfroguser',
//                                     password: 'AdminPassword1',bypassProxy: true,timeout: 300)
//                 rtMavenDeployer (id: "MAVEN_DEPLOYER",serverId: "jenkins-artifactory-server",
//                                     releaseRepo: "spring-petclinic",snapshotRepo: "spring-petclinic-snapshot")
//                 rtMavenResolver (id: "MAVEN_RESOLVER",serverId: "jenkins-artifactory-server",
//                                     releaseRepo: "spring-petclinic",snapshotRepo: "spring-petclinic-snapshot")
//                 }
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
