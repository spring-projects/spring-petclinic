pipeline {
    agent {
        kubernetes {
            defaultContainer "maven"
            yamlFile "jenkins.k8s.yaml"
        }
    }
    tools { 
        maven "Maven 3.8.6"
    }
    options { 
        skipDefaultCheckout() 
    }  
    stages {
        stage("Version") {
            steps {
                container("gitversion") {
                    script {
                        this.checkoutSCM(scm)
                        this.version()
                    }
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
        //         junit "target/surefire-reports/*.xml" 
        //     }
        //   }
        // }

        // stage("SonarQube") {
        //     environment {
        //         scannerHome = tool "sonar"
        //     }
        //     steps {
        //         withSonarQubeEnv(installationName: "sonar") {
        //             sh "mvn sonar:sonar -Dsonar.organization=sergeydz -Dsonar.projectKey=SergeyDz_spring-petclinic"
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

def checkoutSCM(scm) {
    checkout([
        $class: "GitSCM",
        branches: scm.branches,
        doGenerateSubmoduleConfigurations: scm.doGenerateSubmoduleConfigurations,
        extensions: [
            [$class: "CloneOption", reference: "", noTags: false, shallow: false, depth: 0],
            [$class: "RelativeTargetDirectory", relativeTargetDir: ""]
        ],
        userRemoteConfigs: scm.userRemoteConfigs
    ])
}

def version() {
    sh "/tools/dotnet-gitversion"
    def author = sh """
        git config --global --add safe.directory ${workspace}
        git show -s --format=\"%ae\"
    """
    currentBuild.description = "<b>${env.GitVersion_FullSemVer}</b> ${author}"
}