try {
    def appName=env.APP_NAME
    def gitSourceUrl=env.GIT_SOURCE_URL
    def gitSourceRef=env.GIT_SOURCE_REF
    def project=""
    def projectVersion=""
    def quayUser=env.QUAY_USER
    def quayPassword=env.QUAY_PASS
    def ocpUser=env.OCP_USER
    def ocpPassword=env.OCP_PASS

    node('jenkins-slave-skopeo') {
        
        stage('Clair Container Vulnerability Scan') {
            echo "Printing ocp and quay users:"
            echo "OCP: ${ocpUser}"
            echo "Quay: ${quayUser}"
            
            sh "oc login -u $ocpUser -p $ocpPassword --insecure-skip-tls-verify https://api.cluster-ottawa-7b89.ottawa-7b89.example.opentlc.com:6443 2>&1"
            sh 'skopeo --debug copy --src-creds="$(oc whoami)":"$(oc whoami -t)" --src-tls-verify=false --dest-tls-verify=false' + " --dest-creds=$quayUser:$quayPassword docker://docker-registry.default.svc:5000/cicd/petclinic:latest docker://quay.io/$quayUser/petclinic:latest"
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
                    openshift.tag("${appName}:dev", "${appName}:uat")
                }
            }
        }
        stage("Deploy UAT") {
            echo "Deploy to UAT."
            openshift.withCluster() {
                openshift.withProject("${appName}-uat") {
                    echo "Rolling out to UAT."
                    def dc = openshift.selector('dc', "${appName}")
                    dc.rollout().latest()
                    dc.rollout().status()
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
