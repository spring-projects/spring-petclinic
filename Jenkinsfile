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
    }
    /////////////////////////////////////////////////////////////////////
    // END
    /////////////////////////////////////////////////////////////////////

    /////////////////////////////////////////////////////////////////////
    // START : Stages
    /////////////////////////////////////////////////////////////////////
    stages {
        stage('build maven package') {
            steps {
                rtServer (
                    id: "ARTIFACTORY_SERVER",
                    url: "$artifactory_url"
                )
                rtMavenDeployer (
                    id: "MAVEN_DEPLOYER",
                    serverId: "ARTIFACTORY_SERVER",
                    releaseRepo: "spring-petclinic",
                    snapshotRepo: "spring-petclinic-snapshot"
                )

                rtMavenResolver (
                    id: "MAVEN_RESOLVER",
                    serverId: "ARTIFACTORY_SERVER",
                    releaseRepo: "spring-petclinic",
                    snapshotRepo: "spring-petclinic-snapshot"
                )

                sh "mvn validate compile test package"
            }
        }
        stage('build docker image') {
            steps {
                script {
                    dockerImage = docker.build "mpatel011/spring-petclinic:$BUILD_NUMBER"
                }
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
