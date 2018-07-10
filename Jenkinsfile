#!groovy
@Library('github.com/cloudogu/ces-build-lib@9bcafcb')
import com.cloudogu.ces.cesbuildlib.*

properties([
        // Don't run concurrent builds, because the ITs use the same port causing random failures on concurrent builds.
        disableConcurrentBuilds()
])

node {

    cesFqdn = findHostName()
    cesUrl = "https://${cesFqdn}"
    String credentialsId = 'scmCredentials'

    Maven mvn = new MavenWrapper(this)

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
            withCredentials([usernamePassword(credentialsId: credentialsId,
                    passwordVariable: 'PASSWORD', usernameVariable: 'USERNAME')]) {
                //noinspection GroovyAssignabilityCheck
                mvn "org.codehaus.mojo:sonar-maven-plugin:3.2:sonar -Dsonar.host.url=${cesUrl}/sonar " +
                        "-Dsonar.login=${USERNAME} -Dsonar.password=${PASSWORD} -Dsonar.exclusions=target/**"
            }
        }

        stage('Deploy Artifacts') {
            mvn.setDeploymentRepository(cesFqdn, "${cesUrl}/nexus", credentialsId)

            mvn.deployToNexusRepository('-Dmaven.javadoc.failOnError=false')
        }
    }

    // Archive Unit and integration test results, if any
    junit allowEmptyResults: true, testResults: '**/target/failsafe-reports/TEST-*.xml,**/target/surefire-reports/TEST-*.xml'
}

// Init global vars in order to avoid NPE
String cesFqdn = ''
String cesUrl = ''
