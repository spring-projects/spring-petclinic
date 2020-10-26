#!groovy

// "Constants"
String getCesBuildLibVersion() { '1.44.3' }
String getCesBuildLibRepo() { 'https://github.com/cloudogu/ces-build-lib/' }
String getAppDockerRegistry() { 'eu.gcr.io/cloudogu-backend' }
String getCloudoguDockerRegistry() { 'ecosystem.cloudogu.com' }
String getRegistryCredentials() { 'jenkins' }
String getScmManagerCredentials() { 'jenkins' }
String getKubectlImage() { 'lachlanevenson/k8s-kubectl:v1.18.2' }
String getJdkImage() { 'bellsoft/liberica-openjdk-alpine' }
String getHelmImage() { "${cloudoguDockerRegistry}/build-images/helm-kubeval:gcl308.0.0-alpine-helmv3.3.1" }
String getKubevalImage() { 'garethr/kubeval:0.15.0' }
String getGitVersion() { '2.24.3' }
String getConfigRepositoryUrl() { "https://ecosystem.cloudogu.com/scm/repo/backend/k8s-gitops" }
String getConfigRepositoryPRUrl() { 'ecosystem.cloudogu.com/scm/api/v2/pull-requests/backend/k8s-gitops' }
String getDockerRegistryBaseUrl() { "https://console.cloud.google.com/gcr/images/cloudogu-backend/eu/${application}.cloudogu.com" }
String getHelmReleaseChartSourceUrl() { "ssh://k8s-git-ops@ecosystem.cloudogu.com:2222/repo/backend/myCloudogu-helm-chart" }
// Redundant definition of helm repo, because Flux connects via SSH and Jenkins via HTTPS. Best would be to use SSH on Jenkins -> No redundancy
String getHelmChartRepoSourceUrl() { "https://ecosystem.cloudogu.com/scm/repo/backend/myCloudogu-helm-chart" }
String getHelmChartTag() { "1.0.4" }

properties([
        // Don't run concurrent builds, because the ITs use the same port causing random failures on concurrent builds.
        disableConcurrentBuilds()
])

cesBuilbLib = library(identifier: "ces-build-lib@${cesBuildLibVersion}",
        retriever: modernSCM([$class: 'GitSCMSource', remote: cesBuildLibRepo])
).com.cloudogu.ces.cesbuildlib
def git = cesBuilbLib.Git.new(this, scmManagerCredentials)

node {

    mvn = cesBuilbLib.MavenWrapper.new(this)

    catchError {

        stage('Checkout') {
            checkout scm
        }

        stage('Build') {
            mvn 'clean package -DskipTests'

            archiveArtifacts artifacts: '**/target/*.jar'
        }

        String jacoco = "org.jacoco:jacoco-maven-plugin:0.8.1"

        stage('Test') {
            mvn "${jacoco}:prepare-agent test ${jacoco}:report"
        }


        stage('Integration Test') {
            mvn "${jacoco}:prepare-agent-integration failsafe:integration-test failsafe:verify ${jacoco}:report-integration"
        }


        stage('Static Code Analysis') {


        }

        stage('Docker') {
            if (isBuildSuccessful()) {
                def docker = cesBuilbLib.Docker.new(this)

                String imageTag = createImageTag()
                imageName = "docker-registry:9092/petclinic-plain:${imageTag}"
                def dockerImage = docker.build(imageName)
                dockerImage.push()
            } else {
                echo 'Skipping docker push, because build not successful or neither on develop branch nor "forceDeployStaging" param set'
            }
        }

        stage('Deploy') {

        }
    }

    // Archive Unit and integration test results, if any
    junit allowEmptyResults: true, testResults: '**/target/failsafe-reports/TEST-*.xml,**/target/surefire-reports/TEST-*.xml'
}


/**
 * Reflect build parameters version name.
 * This is meant to support GitOps PR reviewers to avoid releasing versions not meant for production.
 */
String createImageTag() {
    def git = cesBuilbLib.Git.new(this)
    String branch = git.simpleBranchName
    String branchSuffix = ""

    if (!"develop".equals(branch)) {
        branchSuffix = "-${branch}"
    }

    return "${new Date().format('yyyyMMddHHmm')}-${git.commitHashShort}${branchSuffix}"
}
