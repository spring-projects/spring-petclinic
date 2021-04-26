pipeline {
  agent any
  parameters {
    string(name: 'SETTINGS', defaultValue: "/var/tmp/settings-docker.xml")
    }
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
    //
    // stage('File Param WA') {
    //   steps {
    //     sh'''
    //     cd /tmp/
    //     '''
    //     writeFile file: 'settings-docker.xml', text: params.SETTINGS
    //     mvn -B -f /tmp/settings-docker.xml  -s /usr/share/maven/ref/settings-docker.xml dependency:resolve
    //   }
    //
    //
    // }

 //    stage('build') {
 //      steps {
 //        script {
 //          docker.image('maven:3.8.1-jdk-8').inside {
 //          sh 'cp /var/jenkins_home/.m2/settings.xml 6859117773ac:/usr/share/maven/ref/'
 //          sh 'mvn -B clean package'
 //        }
 //      }
 //    }
 // }
 // stage("publish to nexus") {
 //             steps {
 //                 script {
 //                     // Read POM xml file using 'readMavenPom' step , this step 'readMavenPom' is included in: https://plugins.jenkins.io/pipeline-utility-steps
 //                     pom = readMavenPom file: "pom.xml";
 //                     // Find built artifact under target folder
 //                     filesByGlob = findFiles(glob: "target/*.${pom.packaging}");
 //                     // Print some info from the artifact found
 //                     echo "${filesByGlob[0].name} ${filesByGlob[0].path} ${filesByGlob[0].directory} ${filesByGlob[0].length} ${filesByGlob[0].lastModified}"
 //                     // Extract the path from the File found
 //                     artifactPath = filesByGlob[0].path;
 //                     // Assign to a boolean response verifying If the artifact name exists
 //                     artifactExists = fileExists artifactPath;
 //
 //                     if(artifactExists) {
 //                         echo "*** File: ${artifactPath}, group: ${pom.groupId}, packaging: ${pom.packaging}, version ${pom.version}";
 //
 //                         nexusArtifactUploader(
 //                             nexusVersion: NEXUS_VERSION,
 //                             protocol: NEXUS_PROTOCOL,
 //                             nexusUrl: NEXUS_URL,
 //                             groupId: pom.groupId,
 //                             version: pom.version,
 //                             repository: NEXUS_REPOSITORY,
 //                             credentialsId: NEXUS_CREDENTIAL_ID,
 //                             artifacts: [
 //                                 // Artifact generated such as .jar, .ear and .war files.
 //                                 [artifactId: pom.artifactId,
 //                                 classifier: '',
 //                                 file: artifactPath,
 //                                 type: pom.packaging],
 //
 //                                 // Lets upload the pom.xml file for additional information for Transitive dependencies
 //                                 [artifactId: pom.artifactId,
 //                                 classifier: '',
 //                                 file: "pom.xml",
 //                                 type: "pom"]
 //                             ]
 //                         );
 //
 //                     } else {
 //                         error "*** File: ${artifactPath}, could not be found";
 //                     }
 //                 }
 //
 //                }
 //            }
            stage('run') {
              steps {
                script {
                  sshagent(credentials: 'e6072e08-87bc-481e-9e4a-55d506546356') {
                    sh 'scp http://localhost:8081/repository/maven-nexus-repo/org/springframework/samples/spring-petclinic/2.4.2/spring-petclinic-2.4.2.jar'
                  }
                  // sh 'curl -L -X GET "http://172.19.0.3:8081/service/rest/v1/search/assets/download?sort=version&repository=maven-nexus-repo&maven.groupId=org.springframework.samples&maven.artifactId=spring-petclinic&maven.extension=jar" -H "accept: application/json"'
                    sh 'java -jar spring-petclinic-2.4.2.jar'
                }
              }
            }
         }
        }
