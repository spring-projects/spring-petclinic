pipeline {
    agent any
    stages {
	    stage('Clone'){
	        steps{

			bat("""
		            git clone https://github.com/AnastasiaDyachkova/spring-petclinic.git
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
}
