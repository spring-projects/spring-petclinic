pipeline {
  agent any
  // parameters {
  //   string(name: 'NEXUS_VERSION', defaultValue: "nexus3")
  //   string(name: 'NEXUS_PROTOCOL', defaultValue: "http")
  //   string(name: 'NEXUS_URL', defaultValue: "172.19.0.3:8081")
  //   string(name: 'NEXUS_REPOSITORY', defaultValue: "maven-nexus-repo")
  //   string(name: 'NEXUS_CREDENTIAL_ID', defaultValue: "e6072e08-87bc-481e-9e4a-55d506546356")
  //   }
  environment {
    NEXUS_VERSION = "nexus3"
    NEXUS_PROTOCOL = "http"
    NEXUS_URL = "172.19.0.3:8081"
    NEXUS_REPOSITORY = "maven-nexus-repo"
    NEXUS_CREDENTIAL_ID = "e6072e08-87bc-481e-9e4a-55d506546356"
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
 stage('checkout') {
   steps {
     checkout scm
 }
}
// stage('Publish') {
//     def pom = readMavenPom file: 'pom.xml'
//     nexusPublisher nexusInstanceId: 'your-nexus-instance-id', \
//         nexusRepositoryId: ${env.NEXUS_REPOSITORY}, \
//         packages: [[$class: 'MavenPackage', \
//         mavenAssetList: [[classifier: '', extension: '', filePath: "target/${pom.artifactId}-${pom.version}.${pom.packaging}"], \
//                          [classifier: 'sources', extension: '', filePath: "target/${pom.artifactId}-${pom.version}-sources.${pom.packaging}"]], \
//         mavenCoordinate: [artifactId: "${pom.artifactId}", \
//         groupId: "${pom.groupId}", \
//         packaging: "${pom.packaging}", \
//         version: "${pom.version}-${env.BUILD_NUMBER}"]]]
// }
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

            nexusArtifactUploader
                            nexusVersion: ${env.NEXUS_VERSION},
                            protocol: ${env.NEXUS_PROTOCOL},
                            nexusUrl: ${env.NEXUS_URL},
                            groupId: pom.groupId,
                            version: pom.version,
                            repository: ${env.NEXUS_REPOSITORY},
                            credentialsId: ${env.NEXUS_CREDENTIAL_ID},
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
                        ;
                    //  } else {
                    //     error "*** File: ${artifactPath}, could not be found";
                    // }
                }
            }
        }
     }
  }
