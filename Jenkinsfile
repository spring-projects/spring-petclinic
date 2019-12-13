try {
    def appName=env.APP_NAME
    def gitSourceUrl=env.GIT_SOURCE_URL
    def gitSourceRef=env.GIT_SOURCE_REF
    def project=""
    def projectVersion=""
    node("maven") {
        stage("Initialize") {
            project = env.PROJECT_NAME
            echo "appName: ${appName}"
            echo "gitSourceUrl: ${gitSourceUrl}"
            echo "gitSourceRef: ${gitSourceRef}"
        }
        stage("Checkout") {
            echo "Checkout source."
            git url: "${gitSourceUrl}", branch: "${gitSourceRef}"
            echo "Read POM info."
            pom = readMavenPom file: 'pom.xml'
            projectVersion = pom.version
        }
        stage("Build JAR") {
            echo "Build the app."
            sh "mvn clean package"
        }
        stage("Quality Check") {
   			sh "mvn clean org.jacoco:jacoco-maven-plugin:prepare-agent install -Dmaven.test.failure.ignore=false"
            sh "mvn sonar:sonar -Dsonar.jacoco.reportPaths=target/coverage-reports/jacoco-unit.exec -Dsonar.host.url=http://sonarqube.cicd.svc:9000"
            // sh "mvn org.cyclonedx:cyclonedx-maven-plugin:makeBom"
            //dependencyTrackPublisher(artifact: 'target/bom.xml', artifactType: 'bom', projectName: "${appName}", projectVersion: "${projectVersion}", synchronous: false)
        }
        stage("Build Image") {
            echo "Build container image."
            // unstash name:"jar"
            openshift.withCluster() {
                openshift.withProject('cicd') {
                    sh "oc start-build ${appName}-s2i-build --from-file=target/app.jar -n cicd --follow"
                }
            }
        }
        stage("Tag DEV") {
            echo "Tag image to DEV"
            openshift.withCluster() {
                openshift.withProject('cicd') {
                    openshift.tag("${appName}:latest", "${appName}:dev")
                }
            }
        }
        stage("Deploy DEV") {
            echo "Deploy to DEV."
            openshift.withCluster() {
                openshift.withProject("${appName}-dev") {
                    echo "Rolling out to DEV."
                    def dc = openshift.selector('dc', "${appName}")
                    dc.rollout().latest()
                    dc.rollout().status()
                }
            }
        }
        stage("Tag for QA") {
            echo "Tag to UAT"
            openshift.withCluster() {
                openshift.withProject('cicd') {
                    openshift.tag("${appName}:dev", "${appName}:qa")
                }
            }
        }
    }
} catch (err) {
    echo "in catch block"
    echo "Caught: ${err}"
    currentBuild.result = 'FAILURE'
    throw err
}
