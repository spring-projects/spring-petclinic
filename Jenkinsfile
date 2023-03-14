pipeline {
    agent {label 'JDK-17'}
    trigger{
        pollSCM('* * * * *')
    }
    stages {
        stage('vcm') {
            steps {
                git url : 'https://github.com/devops-Jenkins-assignment/spring-petclinic-1.git',
                 branch : 'feature'
            }
        }
        stage('Merge Pull Request') {
            steps {
                script {
                    def prNumber = env.CHANGE_ID
                    def repoOwner = env.GIT_AUTHOR_NAME 
                    def repoName = GIT_BRANCH
                    def targetBranch = 'develop'  
                     echo "PR Number : ${prNumber} -repo owner : ${repoOwner} - repo Name : ${repoName}"               
                    // clone the repository
                    checkout([$class: 'GitSCM', branches: [[name: '*/develop']], doGenerateSubmoduleConfigurations: false, extensions: [], submoduleCfg: [], userRemoteConfigs: [[credentialsId: 'your_credentials_id', url: 'your_repo_url']]])
                    // fetch the pull request
                    sh "git fetch origin pull/${prNumber}/head:pr-${prNumber}"
                    // checkout the pull request
                    sh "git checkout pr-${prNumber}"
                    // merge the pull request into the develop branch
                    sh "git merge origin/${targetBranch} --no-ff --no-edit"
                    // push the changes to the remote repository
                    sh "git push origin ${targetBranch}"
                }
            }
        }
    }
}
