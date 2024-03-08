node {
    projectName="mypetclinic";
    dockerRegistryOrg="krishnamanchikalapudi"
    environment { 
        softwareVersion()
    }
    stage('Code') {
        stage('clean') {
            sh """ #!/bin/bash
                rm -rf spring-petclinic
            """
        }
        stage('clone') {
            git branch: 'main', url: 'https://github.com/krishnamanchikalapudi/spring-petclinic.git'
        } // stage: clone
        stage('compile') {
            sh """ #!/bin/bash
                mvn clean install -DskipTests=true
            """
        } // stage: compile
    } // stage: code
    stage('Tests') {
        parallel unitTest: {
            stage ("unitTest") {
                timeout(time: 10, unit: 'MINUTES') {
                    sh """ #!/bin/bash
                        mvn test surefire-report:report

                        echo 'surefire report generated in /target/site/surefire-report.html'
                    """
                } // timeout
            } // stage: unittest
        }, checkstyle: {
            stage ("checkStyle") {
                timeout(time: 2, unit: 'MINUTES') {
                    sh """ #!/bin/bash
                        mvn checkstyle:checkstyle

                        echo 'checkstyle report generated in /target/site/checkstyle.html'
                    """
                } // timeout
            } // stage: validate
        }, codeCoverage: {
            stage ("codeCoverage") {
                timeout(time: 2, unit: 'MINUTES') {
                    sh """ #!/bin/bash
                        mvn jacoco:report
                                    
                        echo 'Jacoco report generated in /target/site/jacoco/index.html'
                    """
                } // timeout
            } // stage: Jacoo
        } // parallel
    } // stage: tests
    stage ("Docker") {
        stage('build') {
            sh """ #!/bin/bash
                # docker login
                # docker image build -f Dockerfile -t petclinic:cli .
                docker image build -f Dockerfile -t ${projectName}:${env.BUILD_ID} .
            """
        } // stage: build
        stage('tag') {
            parallel listContainers: {
                sh """ #!/bin/bash
                    docker container ls -a
                """
            }, listImages: {
                sh """ #!/bin/bash
                    docker image ls -a
                """
            }, tagBuildNumb: {
                    sh """ #!/bin/bash
                        docker tag ${projectName}:${env.BUILD_ID} krishnamanchikalapudi/${projectName}:${env.BUILD_ID}
                    """
            }, tagLatest: {
                sh """ #!/bin/bash
                    docker tag ${projectName}:${env.BUILD_ID} krishnamanchikalapudi/${projectName}:latest
                """
            }
        } // stage: tag
        stage('publish') {
            withCredentials([usernamePassword(credentialsId: 'dockerhub', passwordVariable: 'DOCKER_REGISTRY_PWD', usernameVariable: 'DOCKER_REGISTRY_USER')]) {
                sh """ #!/bin/bash
                    docker login -u $DOCKER_REGISTRY_USER -p $DOCKER_REGISTRY_PWD
                    echo 'Login success'

                    docker push krishnamanchikalapudi/${projectName}:${env.BUILD_ID}
                    docker push krishnamanchikalapudi/${projectName}:latest

                    docker logout
                    echo 'Logut...'
                """
            } // withCredentials: dockerhub
        } // stage: push
        stage('clean') {
            parallel pruneImage: {
                sh """ #!/bin/bash
                    docker image prune -f --filter until=1h
                """
            }, pruneContainer: {
                sh """ #!/bin/bash
                    docker container prune -f --filter until=1h
                """
            }, pruneSystem: {
                sh """ #!/bin/bash
                    docker system prune -f --filter until=1h
                """
            }, rmiImages: {
                sh """ #!/bin/bash
                    docker rmi -f ${projectName}
                """
            }
        } // stage: clean
    } // stage: docker
}
def softwareVersion() {
    sh """ #!/bin/bash
        java -version
        mvn -version
        docker version
        echo '\n'
    """
}