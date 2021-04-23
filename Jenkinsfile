pipeline {
  agent any
  parameters {
    string(name: 'NEXUS_VERSION', defaultValue: "nexus3")
    string(name: 'NEXUS_PROTOCOL', defaultValue: "http")
    string(name: 'NEXUS_URL', defaultValue: "172.19.0.3:8081")
    string(name: 'NEXUS_REPOSITORY', defaultValue: "maven-nexus-repo")
    string(name: 'NEXUS_CREDENTIAL_ID', defaultValue: "e6072e08-87bc-481e-9e4a-55d506546356")
    }
  stages {
    stage('pull') {
      steps {
        git branch: "main",
        credentialsId: 'azima-git-ssh',
        url: 'git://github.com/VSAzima/spring-petclinic'
      }
    }
    stage('build') {
      steps {
        script {
          docker.image('maven:3.8.1-jdk-8').inside {
          sh 'mvn -B clean package'
        }
      }
    }
 }
      stage('push') {
        steps {
          script {
            checkout scm;
            echo "check-check";
            pom = readMavenPom file: "pom.xml";
            filesByGlob = findFiles(glob: "target/*.${pom.packaging}");
            echo "${filesByGlob[0].name} ${filesByGlob[0].path} ${filesByGlob[0].directory} ${filesByGlob[0].length} ${filesByGlob[0].lastModified}"
            artifactPath = filesByGlob[0].path;
            artifactExists = fileExists artifactPath;
            //
            // if(artifactExists) {
            // echo "*** File: ${artifactPath}, group: ${pom.groupId}, packaging: ${pom.packaging}, version ${pom.version}";

            nexusArtifactUploader(${params.NEXUS_VERSION}, ${params.NEXUS_PROTOCOL}, ${params.NEXUS_URL}, pom.groupId, pom.version, ${params.NEXUS_REPOSITORY}, ${params.NEXUS_CREDENTIAL_ID},
                      artifacts: [
                        [artifactId: pom.artifactId,
                        classifier: '',
                        file: artifactPath,
                        type: pom.packaging],
                        [artifactId: pom.artifactId,
                        classifier: '',
                        file: "pom.xml",
                        type: "pom"]
                             ]
                         );
                    //  } else {
                    //     error "*** File: ${artifactPath}, could not be found";
                    // }
                }
            }
        }
     }
  }
