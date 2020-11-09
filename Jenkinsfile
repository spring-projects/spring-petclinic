#!groovy

// "Constants"
String getApplication() {"spring-petclinic-plain" }
String getScmManagerCredentials() { 'scmm-user' }
String getConfigRepositoryUrl() { "http://scmm-scm-manager:9091/scm/repo/cluster/gitops" }
String getConfigRepositoryPRUrl() { 'http://scmm-scm-manager:9091/scm/api/v2/pull-requests/cluster/gitops' }
// The docker daemon cant use the k8s service name, because it is not running inside the cluster
String getDockerRegistryBaseUrl() { "localhost:9092" }
String getCesBuildLibVersion() { '1.44.3' }
String getCesBuildLibRepo() { 'https://github.com/cloudogu/ces-build-lib/' }

cesBuildLib = library(identifier: "ces-build-lib@${cesBuildLibVersion}",
        retriever: modernSCM([$class: 'GitSCMSource', remote: cesBuildLibRepo])
).com.cloudogu.ces.cesbuildlib

properties([
        // Don't run concurrent builds, because the ITs use the same port causing random failures on concurrent builds.
        disableConcurrentBuilds()
])

node {

    mvn = cesBuildLib.MavenWrapper.new(this)

    catchError {

        stage('Checkout') {
            checkout scm
        }

        stage('Build') {
            mvn 'clean package -DskipTests'

            archiveArtifacts artifacts: '**/target/*.jar'
        }

        String jacoco = "org.jacoco:jacoco-maven-plugin:0.8.5"

        stage('Test') {
            mvn "${jacoco}:prepare-agent test ${jacoco}:report"
        }

        stage('Integration Test') {
            mvn "${jacoco}:prepare-agent-integration failsafe:integration-test failsafe:verify ${jacoco}:report-integration"
        }

        String imageName = ""
        stage('Docker') {
            String imageTag = createImageTag()
            imageName = "${dockerRegistryBaseUrl}/${application}:${imageTag}"
            mvn "spring-boot:build-image -DskipTests -Dspring-boot.build-image.imageName=${imageName}"

            if (isBuildSuccessful()) {
                def docker = cesBuildLib.Docker.new(this)
                docker.withRegistry("http://${dockerRegistryBaseUrl}") {
                    def image = docker.image(imageName)
                    image.push()
                }
            } else {
                echo 'Skipping docker push, because build not successful'
            }
        }

        stage('Deploy') {
            if (isBuildSuccessful()) {

                def gitopsConfig = [
                        scmmCredentialsId: scmManagerCredentials,
                        scmmConfigRepoUrl: configRepositoryUrl,
                        scmmPullRequestUrl: configRepositoryPRUrl,
                        updateImages: [
                                [ deploymentFilename: "deployment.yaml",
                                  containerName: "spring-petclinic-plain",
                                  imageName: imageName ]
                        ]
                ]

                String pushedChanges = pushToConfigRepo(gitopsConfig)
                currentBuild.description = createBuildDescription(pushedChanges, imageName)
            } else {
                echo 'Skipping deploy, because build not successful'
            }
        }
    }

    // Archive Unit and integration test results, if any
    junit allowEmptyResults: true, testResults: '**/target/failsafe-reports/TEST-*.xml,**/target/surefire-reports/TEST-*.xml'
}

String pushToConfigRepo(Map gitopsConfig) {

    def git = cesBuildLib.Git.new(this, scmManagerCredentials)
    def changesOnGitOpsRepo = ''
    
    // Query and store info about application repo before cloning into gitops repo  
    def applicationRepo = GitRepo.create(git)

    // Display that Jenkins made the GitOps commits not the application repo author
    git.committerName = 'Jenkins'
    git.committerEmail = 'jenkins@cloudogu.com'

    def configRepoTempDir = '.configRepoTempDir'

    try {

        dir(configRepoTempDir) {

            git url: gitopsConfig.scmmConfigRepoUrl, branch: 'master', changelog: false, poll: false
            git.fetch()

            def repoChanges = new HashSet<String>()
            repoChanges += createApplicationForStageAndPushToBranch 'staging', 'master', applicationRepo, git, gitopsConfig

            git.checkoutOrCreate(application)
            repoChanges += createApplicationForStageAndPushToBranch 'production', application, applicationRepo, git, gitopsConfig

            changesOnGitOpsRepo = aggregateChangesOnGitOpsRepo(repoChanges)

            if (changesOnGitOpsRepo) {
                createPullRequest(gitopsConfig)
            }
        }
    } finally {
        sh "rm -rf ${configRepoTempDir}"
    }

    return changesOnGitOpsRepo
}

private String aggregateChangesOnGitOpsRepo(changes) {
    // Remove empty
    (changes - '')
    // and concat into string
            .join('; ')
}

String createApplicationForStageAndPushToBranch(String stage, String branch, GitRepo applicationRepo, def git, Map gitopsConfig) {

    String commitPrefix = stage == 'staging' ? '[S] ' : ''

    sh "mkdir -p ${stage}/${application}"
    // copy extra resources like sealed secrets
    echo "Copying k8s payload from application repo to gitOps Repo: 'k8s/${stage}/*' to '${stage}/${application}'"
    sh "cp ${env.WORKSPACE}/k8s/${stage}/* ${stage}/${application}/ || true"

    gitopsConfig.updateImages.each {
        updateImageVersion("${stage}/${application}/${it['deploymentFilename']}", it['containerName'], it['imageName'])
    }

    git.add('.')
    if (git.areChangesStagedForCommit()) {
        git.commit(commitPrefix + createApplicationCommitMessage(git, applicationRepo), applicationRepo.authorName, applicationRepo.authorEmail)

        // If some else pushes between the pull above and this push, the build will fail.
        // So we pull if push fails and try again
        git.pushAndPullOnFailure("origin ${branch}")
        return "${stage} (${git.commitHashShort})"
    } else {
        echo "No changes on gitOps repo for ${stage} (branch: ${branch}). Not committing or pushing."
        return ''
    }
}

String createApplicationCommitMessage(def git, def applicationRepo) {
    String issueIds =  (applicationRepo.commitMessage =~ /#\d*/).collect { "${it} " }.join('')

    String[] urlSplit = applicationRepo.repositoryUrl.split('/')
    def repoNamespace = urlSplit[-2]
    def repoName = urlSplit[-1]
    String message = "${issueIds}${repoNamespace}/${repoName}@${applicationRepo.commitHash}"

    return message
}

String createImageTag() {
    def git = cesBuildLib.Git.new(this)
    String branch = git.simpleBranchName
    String branchSuffix = ""

    if (!"develop".equals(branch)) {
        branchSuffix = "-${branch}"
    }

    return "${new Date().format('yyyyMMddHHmm')}-${git.commitHashShort}${branchSuffix}"
}

void updateImageVersion(String deploymentFilePath, String containerName, String newImageTag) {
    def data = readYaml file: deploymentFilePath
    def containers = data.spec.template.spec.containers
    def updateContainer = containers.find {it.name == containerName}
    updateContainer.image = newImageTag
    writeYaml file: deploymentFilePath, data: data, overwrite: true
}

void createPullRequest(Map gitopsConfig) {

    withCredentials([usernamePassword(credentialsId: gitopsConfig.scmmCredentialsId, passwordVariable: 'GIT_PASSWORD', usernameVariable: 'GIT_USER')]) {

        String script =
                'curl -s -o /dev/null -w "%{http_code}" ' +
                        "-u ${GIT_USER}:${GIT_PASSWORD} " +
                        '-H "Content-Type: application/vnd.scmm-pullRequest+json;v=2" ' +
                        '--data \'{"title": "created by service ' + application + '", "source": "' + application + '", "target": "master"}\' ' +
                        gitopsConfig.scmmPullRequestUrl

        // For debugging the quotation of the shell script, just do: echo script
        String http_code = sh returnStdout: true, script: script

        // At this point we could write a mail to the last committer that his commit triggered a new or updated GitOps PR

        echo "http_code: ${http_code}"
        // PR exists if we get 409
        if (http_code != "201" && http_code != "409") {
            unstable 'Could not create pull request'
        }
    }
}

private String createBuildDescription(String pushedChanges, String imageName) {
    String description = ''

    description += "GitOps commits: "

    if (pushedChanges) {
        description += pushedChanges
    } else {
        description += 'No changes'
    }

    description += "\nImage: ${imageName}"

    return description
}

/** Queries and stores info about current repo and HEAD commit */ 
class GitRepo {

    static GitRepo create(git) {
        // Constructors can't be used in Jenkins pipelines due to CPS
        // https://www.jenkins.io/doc/book/pipeline/cps-method-mismatches/#constructors
        return new GitRepo(git.commitAuthorName, git.commitAuthorEmail ,git.commitHashShort, git.commitMessage, git.repositoryUrl)
    }

    GitRepo(String authorName, String authorEmail, String commitHash, String commitMessage, String repositoryUrl) {
        this.authorName = authorName
        this.authorEmail = authorEmail
        this.commitHash = commitHash
        this.commitMessage = commitMessage
        this.repositoryUrl = repositoryUrl
    }

    final String authorName
    final String authorEmail
    final String commitHash
    final String commitMessage
    final String repositoryUrl
}

def cesBuildLib
