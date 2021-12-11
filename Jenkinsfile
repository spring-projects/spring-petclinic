pipeline {
    agent any
    stages {
	    stage('Clone'){
	        steps{

			bat("""
		            git clone https://github.com/hw2cb/spring-petclinic.git
		            """)
	        }
	    }
            stage('Build'){
                steps{
                        bat('C:\\ProgramData\\Jenkins\\.jenkins\\workspace\\petclinic\\build.bat')
                }
	    }
            stage('Archive'){
                steps{
			zip zipFile: "${BUILD_NUMBER}.zip", archive:false, dir: 'target'
			archiveArtifacts artifacts: "${BUILD_NUMBER}.zip"
		}
	    }
            stage('Deploy'){
		steps{
			script{
				try
				{
					bat("md C:\\Deploy\\")
				}catch(Exception e){}
			}
			unzip zipFile: "${BUILD_NUMBER}.zip", dir: 'C:\\Deploy'
		}
	    }
            
    }
    post {
	  always{
		emailext attachLog: true, body: '''$PROJECT_NAME - Build # $BUILD_NUMBER - $BUILD_STATUS:
		Check console output at $BUILD_URL to view the results. ${JELLY_SCRIPT, template="html"}''', subject: '$PROJECT_NAME - Build # $BUILD_NUMBER - $BUILD_STATUS!', to: 'lmidorunl@gmail.com'
	  }
          cleanup {
              cleanWs()
          }
   }
}
