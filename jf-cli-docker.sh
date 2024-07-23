# SET meta-data to differentiate application category, such as application or internal-library
# export PACKAGE_CATEGORIES=(WEBAPP, SERVICE, LIBRARY, BASEIMAGE)

# Config - Artifactory info# https://docs.jfrog-applications.jfrog.io/jfrog-applications/jfrog-cli/configurations/jfrog-platform-configuration 

export JF_RT_URL="https://psazuse.jfrog.io" JFROG_NAME="psazuse" JFROG_RT_USER="krishnam" JF_BEARER_TOKEN="<GET_YOUR_OWN_KEY>" JFROG_CLI_LOG_LEVEL=DEBUG

echo " JFROG_NAME: $JFROG_NAME \n JF_RT_URL: $JF_RT_URL \n JFROG_RT_USER: $JFROG_RT_USER \n JFROG_CLI_LOG_LEVEL: $JFROG_CLI_LOG_LEVEL \n "

## Health check
jf rt ping --url=${JF_RT_URL}/artifactory

# MVN 
## Config - project
### CLI
export BUILD_NAME="spring-petclinic" BUILD_ID="cmd.$(date '+%Y-%m-%d-%H-%M')" PACKAGE_CATEGORY="WEBAPP"

### Jenkins
export BUILD_NAME=${env.JOB_NAME} BUILD_ID=${env.BUILD_ID} PACKAGE_CATEGORY="WEBAPP"
# References: 
# https://www.jenkins.io/doc/book/pipeline/jenkinsfile/#using-environment-variables 
# https://wiki.jenkins.io/JENKINS/Building+a+software+project 

### CMD
export RT_PROJECT_REPO="krishnam-mvn"

echo " BUILD_NAME: $BUILD_NAME \n BUILD_ID: $BUILD_ID \n JFROG_CLI_LOG_LEVEL: $JFROG_CLI_LOG_LEVEL  \n RT_PROJECT_REPO: $RT_PROJECT_REPO  \n "

jf mvnc --server-id-resolve ${JFROG_NAME} --server-id-deploy ${JFROG_NAME} --repo-resolve-releases ${RT_PROJECT_REPO}-virtual --repo-resolve-snapshots ${RT_PROJECT_REPO}-virtual --repo-deploy-releases ${RT_PROJECT_REPO}-local --repo-deploy-snapshots ${RT_PROJECT_REPO}-dev-local

## Audit
jf audit --mvn --extended-table=true

## Create Build
jf mvn clean install -DskipTests=true --build-name=${BUILD_NAME} --build-number=${BUILD_ID} --detailed-summary=true --scan=true

## scan packages
jf scan . --extended-table=true --format=simple-json --server-id=${JFROG_NAME}

## Docker
### config
docker login psazuse.jfrog.io -u krishnam -p <GET_YOUR_OWN_PASSWORD>  

### Create image
docker image build -f Dockerfile-cli -t psazuse.jfrog.io/krishnam-docker-virtual/${BUILD_NAME}:${BUILD_ID} .

#### Tag with latest also
docker tag psazuse.jfrog.io/krishnam-docker-virtual/${BUILD_NAME}:${BUILD_ID} psazuse.jfrog.io/krishnam-docker-virtual/${BUILD_NAME}:latest 

### Push image
jf docker push psazuse.jfrog.io/krishnam-docker-virtual/${BUILD_NAME}:${BUILD_ID} --build-name=${BUILD_NAME} --build-number=${BUILD_ID} --detailed-summary=true

jf docker push psazuse.jfrog.io/krishnam-docker-virtual/${BUILD_NAME}:latest --build-name=${BUILD_NAME} --build-number=${BUILD_ID} --detailed-summary=true

### Scan image
jf docker scan psazuse.jfrog.io/krishnam-docker-virtual/${BUILD_NAME}:${BUILD_ID} --build-name=${BUILD_NAME} --build-number=${BUILD_ID} --detailed-summary=true --extended-table=true --format=simple-json --server-id=${JFROG_NAME}





https://docs.jfrog-applications.jfrog.io/jfrog-applications/jfrog-cli/cli-for-jfrog-artifactory/package-managers-integration#adding-published-docker-images-to-the-build-info


## bdc: build-docker-create, Adding Published Docker Images to the Build-Info 
jf rt bdc krishnam-docker-virtual --image-file=${BUILD_NAME}:${BUILD_ID} --build-name=${BUILD_NAME}  --build-number=${BUILD_ID}


jf rt bdc krishnam-docker-dev-local --image-file=${BUILD_NAME}:${BUILD_ID} --build-name=${BUILD_NAME}  --build-number=${BUILD_ID}







## bp:build-publish - Publish build info
jf rt bp ${BUILD_NAME} ${BUILD_ID} --detailed-summary=true

## bs: Build Scan
jf bs ${BUILD_NAME} ${BUILD_ID} --rescan=true 


## RBv2: release bundle - create
echo " BUILD_NAME: $BUILD_NAME \n BUILD_ID: $BUILD_ID \n RT_PROJECT_REPO: $RT_PROJECT_REPO  \n RT_PROJECT_RB_SIGNING_KEY: $RT_PROJECT_RB_SIGNING_KEY  \n "

echo "{\"builds\": [{\"name\": \"${BUILD_NAME}\", \"number\": \"${BUILD_ID}\"}]}" > build-spec.json && jf rbc --sync=true --url="${JF_RT_URL}" --access-token="${JF_BEARER_TOKEN}" --signing-key="${RT_PROJECT_RB_SIGNING_KEY}" --builds=build-spec.json ${BUILD_NAME} ${BUILD_ID} --spec-vars="key1=value1"


## RBv2: release bundle - DEV promote
jf rbp --sync=true --url="${JF_RT_URL}" --access-token="${JF_BEARER_TOKEN}" --signing-key="${RT_PROJECT_RB_SIGNING_KEY}" ${BUILD_NAME} ${BUILD_ID} DEV

## RBv2: release bundle - QA promote
jf rbp --sync=true --url="${JF_RT_URL}" --access-token="${JF_BEARER_TOKEN}" --signing-key="${RT_PROJECT_RB_SIGNING_KEY}" ${BUILD_NAME} ${BUILD_ID} QA


## RBv2: sign

## Distribute
echo "{\"distribution_rules\": [{\"site_name\": \"psazeuwedge\"}] }" > ds-rbc-spec.json && jf rbd --dist-rules=ds-rbc-spec.json --sync=true --create-repo=true --url="${JF_RT_URL}" --access-token="${JF_BEARER_TOKEN}" ${BUILD_NAME} ${BUILD_ID}




------------------------------------



jf ds rbc ${BUILD_NAME} ${BUILD_ID} --spec=ds-rbc-spec.json --access-token="${JF_BEARER_TOKEN}" --detailed-summary=true --sign=true --spec-vars="key1=value1;key2=value2"

# Curl to get build info
# jf rt curl -XGET '/artifactory/api/build/${BUILD_NAME}/${BUILD_ID}'

curl --location 'https://psazuse.jfrog.io/artifactory/api/build/spring-petclinic/cmd.2024-06-07-16-23' --header 'Content-Type:  application/json' --header 'Authorization: Bearer ${JF_BEARER_TOKEN}' | jq -r '.buildInfo.properties'

BP_RESP_DATA=$(curl --location 'https://psazuse.jfrog.io/artifactory/api/build/spring-petclinic/cmd.2024-06-07-16-23' --header 'Content-Type:  application/json' --header 'Authorization: Bearer ${JF_BEARER_TOKEN}'  | jq -r '.buildInfo.properties')

echo $BP_RESP_DATA | jq -r 'buildInfo.env.PACKAGE_CATEGORY'

# bs:build-scan - Scan a published build-info with Xray.  https://psazuse.jfrog.io/xray/api/v2/ci/build
# jf rt bs spring-petclinic ${BUILD_ID}
jf bs ${BUILD_NAME} ${BUILD_ID} --rescan=true --fail=false

# rbc:release-bundle-create - Create a release bundle from builds or from existing release bundles
jf rbc ${BUILD_NAME} ${BUILD_ID}


echo "{\"builds\": [{\"name\": \"spring-petclinic\", \"number\": \"cmd.2024.05.10.14.10\"}]}" > build-spec.json && jf rbc --sync=true --signing-key=krishnam  --builds=build-spec.json "spring-petclinic-bundle" 05.10.14.10


# bpr:build-promote - This command is used to promote build in Artifactory.
jf rt bpr ${BUILD_NAME}${BUILD_ID} krishnam-mvn-qa-local



# rbp:release-bundle-promote - Promote a release bundle
jf rbp ${BUILD_NAME}${BUILD_ID} QA