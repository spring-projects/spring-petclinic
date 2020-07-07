node('master'){
    stage('scm'){
	git 'https://github.com/ametgud4u/spring-petclinic.git'
    }

    stage('build'){
	sh label: '', script: 'mvn clean package'
    }

    stage('Sonar') {
        withSonarQubeEnv('sonar') {
        sh 'mvn org.sonarsource.scanner.maven:sonar-maven-plugin:3.6.0.1398:sonar'
    }

      stage("Quality Gate"){
          timeout(time: 35, unit: 'MINUTES') {
              def qg = waitForQualityGate()
              if (qg.status != 'OK') {
                  error "Pipeline aborted due to quality gate failure: ${qg.status}"
               }
       stage('postbuild'){
	junit '**/target/surefire-reports/*.xml'
	archiveArtifacts 'target/*.jar'
    }
            }
        }
    }
    }
