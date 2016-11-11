node {
	// Compilation
	stage 'Compile'

		echo 'toto'
		checkout scm
		
		sh "ls ${workspace}"
		
		def v = version()
		if (v) {
			echo "Building version ${v}"
		}
		
		def mvnHome = tool 'M3'
		sh "${mvnHome}/bin/mvn compile"
		//archiveArtifacts artifacts: '**/target/classes/**', fingerprint: true
		stash includes: '**/target/classes/**', name: 'ClassesFromCompileStage'
		stash includes: '**/src/test/**', name: 'SourcesTestsFromCompileStage'
		stash includes: 'pom.xml', name: 'Pom'
		
		// Tests unitaires sequentiels
		//stage 'TestU'
		
		//sh "${mvnHome}/bin/mvn test"
		//junit '**/target/surefire-reports/TEST-*.xml'
	
	// Sonar
	stage 'Quality'
		
		sh '''
		GIT_BRANCH=`git rev-parse --abbrev-ref HEAD`
		SONAR_BRANCH=`printf '%s' $GIT_BRANCH | sed s/[^0-9a-zA-Z:_.\\-]/'_'/g`
		echo "GIT_BRANCH=${GIT_BRANCH}" > my-build-vars.properties
		echo "SONAR_BRANCH=${SONAR_BRANCH}" >> my-build-vars.properties
		'''    
		def props = getBuildProperties("my-build-vars.properties")
		echo "my-build-vars.properties=${props}"
		def sonarBranchParam = getSonarBranchParameter(props.getProperty('SONAR_BRANCH'))

		sh "${mvnHome}/bin/mvn sonar:sonar ${sonarBranchParam} -Dsonar.exclusions=**/vendors/**,**/tests/**,**/test/** -Dsonar.jdbc.url='jdbc:h2:tcp://localhost:9092;databaseName=sonar' -Dsonar.host.url='http://localhost:9000' -Dsonar.jdbc.username=sonar -Dsonar.jdbc.password=sonar"

	// Tests unitaires parallelises
	stage 'Unit Tests'
		runTests()
		
	stage 'Package'
		sh "${mvnHome}/bin/mvn package -DskipTests=true"
	    //archiveArtifacts artifacts: '**/target/classes/**', fingerprint: true
	
	stage 'Deploy'
		sh "${mvnHome}/bin/mvn tomcat7:deploy-only -DskipTests=true -Dmaven.tomcat.charset='UTF-8' -Dmaven.tomcat.path='/petclinic' -Dmaven.tomcat.update=true -Dmaven.tomcat.url='http://localhost:9966/manager/text' -DwarFile='${workspacePwd}/target/petclinic.war'"

		input 'Is it ok ?'
		sh "${mvnHome}/bin/mvn tomcat7:undeploy -DskipTests=true -Dmaven.tomcat.path='/petclinic' -Dmaven.tomcat.url='http://localhost:9966/manager/text'"
	
}

// Joue les tests de maniere parallele
def runTests() {

	def mvnHome = tool 'M3'
	def splits = splitTests count(2)
	def testGroups = [:]
	for (int i = 0; i < splits.size(); i++) {
		//def split = splits[i]
		def index = i
		testGroups["split${i}"] = {
			node {
				unstash 'ClassesFromCompileStage'
				unstash 'SourcesTestsFromCompileStage'
				unstash 'Pom'
				
				sh "echo ${i}-${workspace}"
				sh "ls ${workspace}"
				
				def exclusions = splits.get(index);
				writeFile file: 'exclusions.txt', text: exclusions.join("\n")
				
				sh "ls ${workspace}"
				
				def mavenTest = 'test -DMaven.test.failure.ignore=true -Pexclusions'
				sh "${mvnHome}/bin/mvn ${mavenTest}"
				
				junit '**/target/surefire-reports/TEST-*.xml'
			}
		}
	}
	parallel testGroups
}

// Methodes utilitaires pour Maven
def version() {
  def matcher = readFile('pom.xml') =~ '<version>(.+)</version>'
  matcher ? matcher[0][1] : null
}

// Methodes utilitaires pour Sonar
def getSonarBranchParameter(branch) {
    sonarBranchParam = ""
     if ("develop".equals(branch)) {
        echo "branch is develop, sonar.branch not mandatory"
    } else {
        echo "branch is not develop"
        sonarBranchParam="-Dsonar.branch=" + branch
    }
   return sonarBranchParam
}

def Properties getBuildProperties(filename) {
    def properties = new Properties()
    properties.load(new StringReader(readFile(filename)))
    return properties
}

