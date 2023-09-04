prepare:
	test -n "$$EMAIL" || (echo "EMAIL is not set. Please set it first."; exit 1)

build:
	./mvnw package

run: prepare build
	java -javaagent:$$HOME/.m2/repository/com/contrastsecurity/contrast-agent/5.2.3/contrast-agent-5.2.3.jar -Dapplication.name=petclinic-${EMAIL} -Dcontrast.config.path=contrast.yaml -jar target/*.jar