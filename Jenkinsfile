#!groovy
node {

    cesFqdn = findHostName()
    cesUrl = "https://${cesFqdn}"
    credentials = usernamePassword(credentialsId: 'scmCredentials', passwordVariable: 'PASSWORD', usernameVariable: 'USERNAME')

    catchError {

        stage('Checkout') {
            checkout scm
        }

        stage('Build') {
            mvn "-DskipTests clean package"

            // archive artifact
            archiveArtifacts artifacts: '**/target/*.jar', fingerprint: true
        }

        parallel(
                test: {
                    stage('Test') {
                        String jacoco = "org.jacoco:jacoco-maven-plugin:0.7.7.201606060606"
                        mvn "${jacoco}:prepare-agent test ${jacoco}:report"
                    }
                },
                integrationTest: {
                    stage('Integration Test') {
                        String jacoco = "org.jacoco:jacoco-maven-plugin:0.7.7.201606060606";
                        mvn "${jacoco}:prepare-agent-integration failsafe:integration-test ${jacoco}:report-integration"
                    }
                }
        )

        stage('SonarQube Analysis') {
            withCredentials([credentials]) {
                //noinspection GroovyAssignabilityCheck
                mvn "org.codehaus.mojo:sonar-maven-plugin:3.2:sonar -Dsonar.host.url=${cesUrl}/sonar " +
                        "-Dsonar.login=${USERNAME} -Dsonar.password=${PASSWORD} -Dsonar.exclusions=target/**"
            }
        }

        stage('Deploy Artifacts') {
            String releaseProp = "-DaltReleaseDeploymentRepository=${cesFqdn}::default::${cesUrl}/nexus/content/repositories/releases/";
            String snapshotProp = "-DaltSnapshotDeploymentRepository=${cesFqdn}::default::${cesUrl}/nexus/content/repositories/snapshots/";
            mvn "-DskipTests deploy ${releaseProp} ${snapshotProp}"
        }
    }

    // Archive Unit and integration test results, if any
    junit allowEmptyResults: true, testResults: '**/target/failsafe-reports/TEST-*.xml,**/target/surefire-reports/TEST-*.xml'
}

String cesFqdn
String cesUrl
def credentials

void mvn(String args) {
    writeSettingsXml()
    sh "./mvnw -s settings.xml --batch-mode -V -U -e -Dsurefire.useFile=false ${args}"
    sh 'rm -f settings.xml'
}

String findHostName() {
    String regexMatchesHostName = 'https?://([^:/]*)'

    // Storing matcher in a variable might lead to java.io.NotSerializableException: java.util.regex.Matcher
    if (!(env.JENKINS_URL =~ regexMatchesHostName)) {
        script.error 'Unable to determine hostname from env.JENKINS_URL. Expecting http(s)://server:port/jenkins'
    }
    return (env.JENKINS_URL =~ regexMatchesHostName)[0][1]
}

void writeSettingsXml() {
    withCredentials([credentials]) {
        writeFile file: "settings.xml", text: """
            <settings>
                <localRepository>${env.HOME}/.m2/repository</localRepository>
                <servers>
                    <server>
                      <id>${cesFqdn}</id>
                      <username>${USERNAME}</username>
                      <password>${PASSWORD}</password>
                    </server>
                </servers>
                <mirrors>
                    <mirror>
                      <id>${cesFqdn}</id>
                      <name>${cesFqdn} Central Mirror</name>
                      <url>${cesUrl}/nexus/content/groups/public</url>
                      <mirrorOf>central</mirrorOf>
                    </mirror>
                </mirrors>
            </settings>"""
    }
}
