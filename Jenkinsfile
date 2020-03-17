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
        script {
          pom = readMavenPom file: 'pom.xml'
          projectVersion = pom.version
        }
      }
    }
    stage('Build JAR') {
      steps {
	echo "Build app.  Version: ${projectVersion}"
        sh "mvn clean package"
      }
    }
    stage('Build Image') {
      steps {
        script {
          echo "Build container image."
          openshift.withCluster() {
            openshift.withProject('cicd') {
              sh "oc start-build ${appName}-build --from-file=target/app.jar -n cicd --follow"
            }
          }
        }
      }
    }
    stage("Tag DEV") {
      steps {
        script {
	  echo "Tag image to DEV"
	  openshift.withCluster() {
	    openshift.withProject('cicd') {
	      openshift.tag("${appName}:latest", "${appName}:dev")
	    }
	  }
	}
      }
    }
    stage('Deploy DEV') {
      steps {
        script {
          echo "Deploy to DEV."
          openshift.withCluster() {
            openshift.withProject("demo-dev") {
              echo "Rolling out to DEV."
              def dc = openshift.selector('dc', "${appName}")
              dc.rollout().latest()
              dc.rollout().status()
            }
          }
        }
      }
    }
    stage("Tag TEST") {
      steps {
        script {
	  echo "Tag image to TEST"
	  openshift.withCluster() {
	    openshift.withProject('cicd') {
	      openshift.tag("${appName}:dev", "${appName}:test")
	    }
	  }
	}
      }
    }
    stage('Deploy TEST') {
      steps {
        script {
          echo "Deploy to TEST."
          openshift.withCluster() {
            openshift.withProject("demo-test") {
              echo "Rolling out to TEST."
              def dc = openshift.selector('dc', "${appName}")
              dc.rollout().latest()
              dc.rollout().status()
            }
          }
        }
      }
    }
  }
}
