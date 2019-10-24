pipeline {
    agent {
        label 'master'
    }

    stages {
        stage('init') {
            steps {
                git url: "git@github.com:azure-devops/spring-petclinic.git",
                credentialsId: "github_ssh_key",
                branch: "ignite"

                script {
                    env.SHA = sh(script: 'git rev-parse --short HEAD', returnStdout: true).trim()
                    env.GIT_TAG = sh(script: 'git tag -l --points-at HEAD', returnStdout: true).trim()
                    if (env.GIT_TAG == '') {
                        env.IMAGE_TAG = SHA
                        env.DEPLOY_TO = "staging"
                    } else {
                        env.IMAGE_TAG = env.GIT_TAG
                        env.DEPLOY_TO = "production"
                    }
                }
            }
        }

        stage('build') {
            when {
                changeset "src/**"
            }

            steps {
                sh '''
                    ./mvnw clean package
                    mv target/*.jar target/pet-clinic.jar
                '''
            }
        }


        stage('image build') {
            when {
                changeset "src/**"
            }

            steps {
                acrQuickTask azureCredentialsId: "jenkins-sp",
                                registryName: "jenkinsdemosacr",
                                resourceGroupName: "demo-aks",
                                local: "",
                                dockerfile: "Dockerfile",
                                imageNames: [[image: "jenkinsdemosacr.azurecr.io/pet-clinic:pc-${IMAGE_TAG}"]]
            }
        }

        stage('update staging config') {
            when {
                changeset "src/**"
                environment name: "DEPLOY_TO", value: 'staging'
            }
            steps {
                dir('infra/kube/workloads/staging') {
                    sh '''
                        sed -i -e "s/pc-......./pc-\${IMAGE_TAG}/" deployment.yaml
                        sed -i -e "s/pc-......./pc-\${IMAGE_TAG}/" service.yaml
                        git add *
                        git commit -m "Update staging config with \${IMAGE_TAG} commit"
                        git push origin ignite
                    '''
                }
            }
        }

        stage('update production config') {
            when {
                changeset "src/*"
                environment name: "DEPLOY_TO", value: 'production'
            }
            steps {
                dir('infra/kube/workloads/production') {
                    sh '''
                        sed -i -e "s/v[0-9]\\.[0-9]\\.[0-9]/\${IMAGE_TAG}/" deployment.yaml
                        sed -i -e "s/v[0-9]\\.[0-9]\\.[0-9]/\${IMAGE_TAG}/" service.yaml
                        git add *
                        git commit -m "Update production config with \${IMAGE_TAG} commit"
                        git push origin ignite
                    '''
                }
            }
        }
    }
}
