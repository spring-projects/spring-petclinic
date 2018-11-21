pipeline {
    agent any
    environment {
      ORG               = 'rverduci-test-org'
      APP_NAME          = 'spring-petclinic'
      CHARTMUSEUM_CREDS = credentials('jenkins-x-chartmuseum')
    }
    stages {
      stage('CI Build and push snapshot') {
        when {
          branch 'PR-*'
        }
        environment {
          PREVIEW_VERSION = "0.0.0-SNAPSHOT-$BRANCH_NAME-$BUILD_NUMBER"
          PREVIEW_NAMESPACE = "$APP_NAME-$BRANCH_NAME".toLowerCase()
          HELM_RELEASE = "$PREVIEW_NAMESPACE".toLowerCase()
        }
        steps {

          sh "mvn versions:set -DnewVersion=$PREVIEW_VERSION"
          sh "mvn install"
          sh 'export VERSION=$PREVIEW_VERSION && skaffold build -f skaffold.yaml'

          sh "jx step validate --min-jx-version 1.2.36"
          sh "jx step post build --image \$DOCKER_REGISTRY/$ORG/$APP_NAME:$PREVIEW_VERSION"


          dir ('./charts/preview') {

             sh "make preview"
             sh "jx preview --app $APP_NAME --dir ../.."

          }
        }
      }
      stage('Build Release') {
        when {
          branch 'master'
        }
        steps {

            git 'https://github.com/rverduci-test-org/spring-petclinic.git'

            // so we can retrieve the version in later steps
            sh "echo \$(jx-release-version) > VERSION"
            sh "mvn versions:set -DnewVersion=\$(cat VERSION)"

            dir ('./charts/spring-petclinic') {
              sh "make tag"
            }

            sh 'mvn clean deploy'

            sh 'export VERSION=`cat VERSION` && skaffold build -f skaffold.yaml'

            sh "jx step post build --image \$DOCKER_REGISTRY/$ORG/$APP_NAME:\$(cat VERSION)"

        }
      }
      stage('Promote to Environments') {
        when {
          branch 'master'
        }
        steps {
          dir ('./charts/spring-petclinic') {
              sh 'jx step changelog --version v\$(cat ../../VERSION)'
              // release the helm chart
              sh 'make release'
              // promote through all 'Auto' promotion Environments
              sh 'jx promote -b --all-auto --timeout 1h --version \$(cat ../../VERSION) --no-wait'
          }
        }
      }
    }
  }
