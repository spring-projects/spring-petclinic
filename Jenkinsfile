// Pipeline Code for SpringPetClinic using Java11 and Maven
// Exercise part of Jenkins Declarative Pipeline Learning
pipeline {
    agent {label 'UBUNTU-JDK11-MVN'}
    parameters {
        choice(name: 'BRANCH_TO_BUILD', choices: ['REL_INT_3.0', 'main'], description: 'Branch to build')        
        choice(name: 'GOAL', choices: ['compile', 'package', 'clean package'])
    }
//  Section defining different stages of build and actions if any       
    
    stages {
        stage ('Artifactory Configuration') {
            steps {           
                rtMavenDeployer (
                    id: 'JFROG_USORAMA',
                    serverId: 'artifactory-server-id',
                    releaseRepo: 'qtusorama-libs-release-local',
                    snapshotRepo: 'qtusorama-libs-snapshot-local',
                    threads: 6,
                    properties: ['BinaryPurpose=Technical-BlogPost', 'Team=DevOps-Acceleration']
                    releaseRepo: 'qtusorama-libs-release-local',
                    snapshotRepo: 'qtusorama-libs-snapshot-local'
                )
            }
        }        
        stage('get code') {
            steps {
                git branch: "REL_INT_3.0", url: 'https://github.com/usorama/spring-petclinic.git'
            }
        }
        stage('build') {
            steps {
                rtMavenRun (
                    // Tool name from Jenkins configuration.
                    tool: 'MVN_DEFAULT',
                    pom: 'pom.xml',
                    goals: 'clean install',
                    // Maven options.
                    deployerId: 'spc-deployer',
                )                
            }
        }
        stage('Archive test results') {
            steps {
                junit testResults: '**/surefire-reports/*.xml'
            }
        }
        
    }            
}
