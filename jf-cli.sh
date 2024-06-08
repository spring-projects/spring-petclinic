# SET meta-data to differentiate application category, such as application or internal-library
# export PACKAGE_CATEGORIES=(APPLICATION LIBRARY)

# export BUILD_ID="cmd.$(date '+%Y-%m-%d-%H-%M')"
export BUILD_NAME="spring-petclinic" && export BUILD_ID="cmd.$(date '+%Y-%m-%d-%H-%M')" && export JFROG_CLI_LOG_LEVEL="DEBUG" && export PACKAGE_CATEGORY='LIBRARY' & export JF_BEARER_TOKEN=''

# MVN 
#jf rt mvn clean install -DskipTests=true --build-name=spring-petclinic --build-number=${BUILD_ID} --detailed-summary=true --scan=true
jf mvn clean install -DskipTests=true --build-name=${BUILD_NAME} --build-number=${BUILD_ID} --detailed-summary=true --scan=true

# bce:build-collect-env - Collect environment variables. Environment variables can be excluded using the build-publish command.
jf rt bce ${BUILD_NAME} ${BUILD_ID} 

# bag:build-add-git - Collect VCS details from git and add them to a build.
jf rt bag ${BUILD_NAME} ${BUILD_ID}

# bp:build-publish - Publish build info
jf rt bp ${BUILD_NAME} ${BUILD_ID} --detailed-summary=true


# Curl to get build info
# jf rt curl -XGET '/artifactory/api/build/${BUILD_NAME}/${BUILD_ID}'
curl --location 'https://psazuse.jfrog.io/artifactory/api/build/spring-petclinic/cmd.2024-06-07-16-23' --header 'Content-Type:  application/json' --header 'Authorization: Bearer ${JF_BEARER_TOKEN}' | jq -r '.buildInfo.properties'

BP_RESP_DATA=$(curl --location 'https://psazuse.jfrog.io/artifactory/api/build/spring-petclinic/cmd.2024-06-07-16-23' --header 'Content-Type:  application/json' --header 'Authorization: Bearer ${JF_BEARER_TOKEN}'  | jq -r '.buildInfo.properties')

echo $BP_RESP_DATA | jq -r 'buildInfo.env.PACKAGE_CATEGORY'

# bs:build-scan - Scan a published build-info with Xray.  https://psazuse.jfrog.io/xray/api/v2/ci/build
# jf rt bs spring-petclinic ${BUILD_ID}
jf bs ${BUILD_NAME}${BUILD_ID} --rescan=true --fail=false

# rbc:release-bundle-create - Create a release bundle from builds or from existing release bundles
jf rbc ${BUILD_NAME}${BUILD_ID}


echo "{\"builds\": [{\"name\": \"spring-petclinic\", \"number\": \"cmd.2024.05.10.14.10\"}]}" > build-spec.json && jf rbc --sync=true --signing-key=krishnam  --builds=build-spec.json "spring-petclinic-bundle" 05.10.14.10


# bpr:build-promote - This command is used to promote build in Artifactory.
jf rt bpr ${BUILD_NAME}${BUILD_ID} krishnam-mvn-qa-local



# rbp:release-bundle-promote - Promote a release bundle
jf rbp ${BUILD_NAME}${BUILD_ID} QA