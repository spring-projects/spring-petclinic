def appName=env.APP_NAME
def gitSourceUrl=env.GIT_SOURCE_URL
def gitSourceRef=env.GIT_SOURCE_REF
def project=env.PROJECT_NAME
def projectVersion=""	

pipeline {
  agent {
    label 'maven'
  }
  stages {

    stage('Initialize') {
      steps {
        echo "appName: ${appName}"
        echo "gitSourceUrl: ${gitSourceUrl}"
        echo "gitSourceRef: ${gitSourceRef}"
      }
    }
    stage('Checkout') {
      steps {
        echo "Checkout source."
        git url: "${gitSourceUrl}", branch: "${gitSourceRef}"
        echo "Read POM info."
        pom = readMavenPom file: 'pom.xml'
        projectVersion = pom.version
      }
    }
    stage('Build JAR') {
      steps {
        echo "Build the app."
        sh "mvn clean package"
      }
    }
    stage('Quality Check') {
      steps {
		sh "mvn clean org.jacoco:jacoco-maven-plugin:prepare-agent install -Dmaven.test.failure.ignore=false"
        sh "mvn sonar:sonar -Dsonar.jacoco.reportPaths=target/coverage-reports/jacoco-unit.exec -Dsonar.host.url=http://sonarqube.cicd.svc:9000"
        // sh "mvn org.cyclonedx:cyclonedx-maven-plugin:makeBom"
        //dependencyTrackPublisher(artifact: 'target/bom.xml', artifactType: 'bom', projectName: "${appName}", projectVersion: "${projectVersion}", synchronous: false)
      }
    }
    stage('Build Image') {
      steps {
        echo "Build container image."
        openshift.withCluster() {
            openshift.withProject('cicd') {
                sh "oc start-build ${appName}-s2i-build --from-file=target/app.jar -n cicd --follow"
            }
        }
      }
    }
    stage('Tag DEV') {
      agent {
          label 'jenkins-slave-skopeo'
      }
      steps {
	    script {
	      openshift.withCluster() {
	        withCredentials([usernamePassword(credentialsId: "${openshift.project()}-quay-creds-secret", usernameVariable: "QUAY_USERNAME", passwordVariable: "QUAY_PASSWORD")]) {
	          sh "skopeo copy docker://quay.io/${QUAY_USERNAME}/${QUAY_REPOSITORY}:latest docker://quay.io/${QUAY_USERNAME}/${QUAY_REPOSITORY}:dev --src-creds \"$QUAY_USERNAME:$QUAY_PASSWORD\" --dest-creds \"$QUAY_USERNAME:$QUAY_PASSWORD\" --src-tls-verify=false --dest-tls-verify=false"
	        }        
	      }
	    }
	  }                       
    }
    stage('Deploy DEV') {
      steps {
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
    }
    stage('Integration Tests') {
      echo "Running Integration tests..."
      sh "mvn verify -Pfailsafe"
    }
    stage('Tag UAT') {
      agent {
          label 'jenkins-slave-skopeo'
      }
      script {
        openshift.withCluster() {
          withCredentials([usernamePassword(credentialsId: "${openshift.project()}-quay-creds-secret", usernameVariable: "QUAY_USERNAME", passwordVariable: "QUAY_PASSWORD")]) {
            sh "skopeo copy docker://quay.io/${QUAY_USERNAME}/${QUAY_REPOSITORY}:dev docker://quay.io/${QUAY_USERNAME}/${QUAY_REPOSITORY}:uat --src-creds \"$QUAY_USERNAME:$QUAY_PASSWORD\" --dest-creds \"$QUAY_USERNAME:$QUAY_PASSWORD\" --src-tls-verify=false --dest-tls-verify=false"
          }
        }
      }                       
    }
    stage('Deploy UAT') {
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
}
