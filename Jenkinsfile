pipeline {
    agent any

    stages {
        stage('init') {
            steps {
                git url: "git@github.com:azure-devops/spring-petclinic.git",
                credentialsId: "github_ssh_key",
                branch: "ignite"
            }
        }

        stage('build') {
            steps {
                sh '''
                    ./mvnw clean package
                    mv target/*.jar target/pet-clinic.jar
                '''
            }
        }


        stage('image build') {
            environment {
               sha = sh(script: 'git rev-parse --short HEAD', returnStdout: true)
            }

            steps {
                acrQuickTask azureCredentialsId: "jenkins-sp",
                                registryName: "jenkinsdemosacr",
                                resourceGroupName: "demo-aks",
                                local: "",
                                dockerfile: "Dockerfile",
                                imageNames: [[image: "jenkinsdemosacr.azurecr.io/pet-clinic:master-${sha}"]]
            }
        }

        stage('update staging config') {
            environment {
               sha = sh(script: 'git rev-parse --short HEAD', returnStdout: true).trim()
            }

            steps {
                dir('infra/kube/workloads/staging') {
                    sh '''
                        sed -i -e "s/master-......./master-\${sha}/" deployment.yaml
                        sed -i -e "s/master-......./master-\${sha}/" service.yaml
                        git add *
                        git commit -m "Update staging file with \${sha} commit"
                        git push origin ignite
                    '''
                }
            }
        }
    }
}
