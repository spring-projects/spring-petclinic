#!/bin/env groovy
pipeline {
  agent none
  environment {
    IMAGE = "liatrio/petclinic-tomcat"
  }
  stages {
    stage('Build') {
      agent {
        docker {
          image 'maven:3.5.0'
          args '-e INITIAL_ADMIN_USER -e INITIAL_ADMIN_PASSWORD --network=${LDOP_NETWORK_NAME}'
        }
      }
      steps {
        configFileProvider(
          [configFile(fileId: 'nexus', variable: 'MAVEN_SETTINGS')]) {
            sh 'mvn -s $MAVEN_SETTINGS clean deploy -DskipTests=true -B'
          }
        }
      }
      stage('Sonar') {
        agent  {
          docker {
            image 'sebp/sonar-runner'
            args '-e SONAR_ACCOUNT_LOGIN -e SONAR_ACCOUNT_PASSWORD -e SONAR_DB_URL -e SONAR_DB_LOGIN -e SONAR_DB_PASSWORD --network=${LDOP_NETWORK_NAME}'
          }
        }
        steps {
          sh '/opt/sonar-runner-2.4/bin/sonar-runner -e -D sonar.login=${SONAR_ACCOUNT_LOGIN} -D sonar.password=${SONAR_ACCOUNT_PASSWORD} -D sonar.jdbc.url=${SONAR_DB_URL} -D sonar.jdbc.username=${SONAR_DB_LOGIN} -D sonar.jdbc.password=${SONAR_DB_PASSWORD}'
        }
      }
      stage('Get Artifact') {
        agent {
          docker {
            image 'maven:3.5.0'
            args '-e INITIAL_ADMIN_USER -e INITIAL_ADMIN_PASSWORD --network=${LDOP_NETWORK_NAME}'
          }
        }
        steps {
          sh 'mvn clean'
          script {
            pom = readMavenPom file: 'pom.xml'
            if (params.version == 'UNSPECIFIED') {
              getArtifact(pom.groupId, pom.artifactId, pom.version)
              } else {
                getArtifact(pom.groupId, pom.artifactId, params.VERSION)
              }
            }
          }
        }
        stage('Build container') {
          agent any
          steps {
            sh 'docker build -t petclinic-tomcat .'
          }
        }
        stage('Run local container') {
          agent any
          steps {
            sh 'docker rm -f petclinic-tomcat-temp || true'
            sh 'docker run -d --network=${LDOP_NETWORK_NAME} --name petclinic-tomcat-temp petclinic-tomcat'
          }
        }
        stage('Smoke-Test & OWASP Security Scan') {
          agent {
            docker {
              image 'maven:3.5.0'
              args '--network=${LDOP_NETWORK_NAME}'
            }
          }
          steps {
            sh "cd regression-suite"
            sh "mvn clean -B test -DPETCLINIC_URL=http://petclinic-tomcat:8080/petclinic/"
          }
        }
        stage('Stop local container') {
          agent any
          steps {
            sh 'docker rm -f petclinic-tomcat-temp || true'
          }
        }
        stage('Deploy to dev') {
          agent any
          steps {
            deployToEnvironment("ec2-user", "dev-petclinic.liatr.io", "petclinic-deploy-key", "${env.IMAGE}", "${env.TAG}", "spring-petclinic", "dev-petclinic.liatr.io/petclinic")
          }
        }
        stage('Smoke test dev') {
          agent {
            docker {
              image 'maven:3.5.0'
              args '--network=${LDOP_NETWORK_NAME}'
            }
          }
          steps {
            sh "cd regression-suite"
            sh "mvn clean -B test -DPETCLINIC_URL=http://dev-petclinic:8080/petclinic/"
            echo "Should be accessible at http://localhost:18888/petclinic"
          }
        }
        stage('Deploy to qa') {
          agent any
          steps {
            deployToEnvironment("ec2-user", "qa-petclinic.liatr.io", "petclinic-deploy-key", "${env.IMAGE}", "${env.TAG}", "spring-petclinic", "qa-petclinic.liatr.io/petclinic")
          }
        }
        stage('Smoke test qa') {
          agent {
            docker {
              image 'maven:3.5.0'
              args '--network=${LDOP_NETWORK_NAME}'
            }
          }
          steps {
            sh "cd regression-suite"
            sh "mvn clean -B test -DPETCLINIC_URL=http://qa-petclinic:8080/petclinic/"
            echo "Should be accessible at http://localhost:18889/petclinic"
            input 'Deploy to Prod?'
          }
        }
        stage('Deploy to ProdA-Green') {
          agent any
          steps {
            deployToEnvironment("ec2-user", "prod-petclinic.liatr.io", "petclinic-deploy-key", "${env.IMAGE}", "${env.TAG}", "spring-petclinic", "prod-petclinic.liatr.io/petclinic", "petclinic-prod")
          }
        }
        stage('Smoke test ProdA-Green') {
          agent {
            docker {
              image 'maven:3.5.0'
              args '--network=${LDOP_NETWORK_NAME}'
            }
          }
          steps {
            sh "cd regression-suite"
            sh "mvn clean -B test -DPETCLINIC_URL=http://proda-petclinic-green:8080/petclinic/"
          }
        }
        stage('Deploy to ProdA') {
          agent any
          steps {
            sh 'docker rm -f proda-petclinic || true'
            sh 'docker container rename proda-petclinic-green proda-petclinic'
          }
        }
        stage('Deploy to ProdB-Green') {
          agent any
          steps {
            deployToEnvironment("ec2-user", "prod-petclinic.liatr.io", "petclinic-deploy-key", "${env.IMAGE}", "${env.TAG}", "spring-petclinic", "prod-petclinic.liatr.io/petclinic", "petclinic-prod")
          }
        }
        stage('Smoke test ProdB-Green') {
          agent {
            docker {
              image 'maven:3.5.0'
              args '--network=${LDOP_NETWORK_NAME}'
            }
          }
          steps {
            sh "cd regression-suite"
            sh "mvn clean -B test -DPETCLINIC_URL=http://prodb-petclinic-green:8080/petclinic/"
          }
        }
        stage('Deploy to ProdB') {
          agent any
          steps {
            sh 'docker rm -f prodb-petclinic || true'
            sh 'docker container rename prodb-petclinic-green prodb-petclinic'
          }
        }
      }
    }
