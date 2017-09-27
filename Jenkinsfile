#!groovy
node {

    cesFqdn = "ecosystem.cloudogu.net";
    cesUrl = "https://${cesFqdn}";
    credentials = usernamePassword(credentialsId: 'scmCredentials', passwordVariable: 'PASSWORD', usernameVariable: 'USERNAME');

    stage('Checkout') {
      checkout scm
    }

    stage('Build') {
      mvn "-DskipTests clean package"

      // archive artifact
      archiveArtifacts artifacts: '**/target/*.jar', fingerprint: true
    }

    stage('Test') {
      String jacoco = "org.jacoco:jacoco-maven-plugin:0.7.7.201606060606";
      mvn "${jacoco}:prepare-agent verify ${jacoco}:report"

      // Archive JUnit results, if any
      junit allowEmptyResults: true, testResults: '**/target/surefire-reports/TEST-*.xml'
    }
    
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

    stage('Deploy Application') {
      sh "ansible-playbook playbook.yaml"
    }
}

String cesFqdn;
String cesUrl;
def credentials;

void mvn(String args) {
  writeSettingsXml()
  sh "./mvnw -s settings.xml --batch-mode -V -U -e -Dsurefire.useFile=false ${args}"
  sh 'rm -f settings.xml'
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
