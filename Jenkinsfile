pipeline {
    agent {
        kubernetes {
            defaultContainer "maven"
            yamlFile "jenkins.k8s.yaml"
        }
    }
    tools { 
        maven 'Maven 3.8.6'
    }
    options { 
        skipDefaultCheckout() 
    }  
    stages {
        stage('Version') {
            steps {
                container("gitversion") {
                    script {
                        Map cloneOptions = [noTags: false, shallow: false, depth: 0]
                        def extensions = [
                            [$class: 'CloneOption', reference: '', noTags: false, shallow: false, depth: 0],
                            [$class: 'RelativeTargetDirectory', relativeTargetDir: targetDirectory]
                        ]
                        checkout([
                            $class: 'GitSCM',
                            branches: scm.branches,
                            doGenerateSubmoduleConfigurations: scm.doGenerateSubmoduleConfigurations,
                            extensions: extensions,
                            userRemoteConfigs: scm.userRemoteConfigs
                        ])
                    }

                    sh "/tools/dotnet-gitversion"
                }
            }
        }
        // stage("Build") {
        //   steps {
        //       sh "mvn clean package -DskipTests=true"
        //   }
        // }

        // stage("Test") {
        //   steps {
        //       sh "mvn test"
        //   }
        //   post {
        //     always {
        //         junit 'target/surefire-reports/*.xml' 
        //     }
        //   }
        // }

        // stage("SonarQube") {
        //     environment {
        //         scannerHome = tool 'sonar'
        //     }
        //     steps {
        //         withSonarQubeEnv(installationName: 'sonar') {
        //             sh 'mvn sonar:sonar -Dsonar.organization=sergeydz -Dsonar.projectKey=SergeyDz_spring-petclinic'
        //         }
        //     }
        // }

        stage("Docker.Build") {
            steps {
                container("docker") {
                    sh "docker build -t sergeydz/spring-petclinic:latest ."
                }
            }
        }
    }
}