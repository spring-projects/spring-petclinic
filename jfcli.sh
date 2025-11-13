
clear

export JF_NAME="psazuse" JFROG_CLI_LOG_LEVEL="DEBUG" 
export JF_RT_URL="https://${JF_NAME}.jfrog.io" RT_REPO_VIRTUAL="springpetclinic-mvn-virtual" 

export BUILD_NAME="spring-petclinic" BUILD_ID="cmd.$(date '+%Y-%m-%d-%H-%M')" 

jf mvnc --global --repo-resolve-releases ${RT_REPO_VIRTUAL} --repo-resolve-snapshots ${RT_REPO_VIRTUAL} 

# jf ca --format=table --threads=100
# 
jf mvn clean install -DskipTests=true -Denforcer.skip=true -f pom.xml --build-name ${BUILD_NAME} --build-number ${BUILD_ID}

jf mvn test -Denforcer.skip=true
python3 ./jfrog/convert/convert_surefire_to_json.py ./target/surefire-reports/test-results.json