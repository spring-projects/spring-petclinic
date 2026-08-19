#!/bin/bash

set -e

PROJECT_NAME="spring-petclinic"
IMAGE_NAME="spring-petclinic:4.0.0"
CONTAINER_NAME="spring-petclinic"
APP_PORT="8081"
SONAR_URL="http://localhost:9000"

echo "======================================"
echo " SPRING PETCLINIC DEVSECOPS CHECK"
echo "======================================"

echo ""
echo "1. GITLEAKS SECURITY SCAN"
echo "--------------------------------------"
gitleaks detect --source . --verbose

echo ""
echo "2. MAVEN TESTS + JACOCO"
echo "--------------------------------------"
mvn clean verify

echo ""
echo "3. VERIFY JACOCO REPORT"
echo "--------------------------------------"

if [ ! -f target/site/jacoco/jacoco.xml ]; then
    echo "ERROR: JaCoCo XML report not found"
    exit 1
fi

ls -lh target/site/jacoco/jacoco.xml

echo ""
echo "4. SONARQUBE ANALYSIS"
echo "--------------------------------------"

if [ -z "$SONAR_TOKEN" ]; then
    echo "ERROR: SONAR_TOKEN is not set"
    exit 1
fi

mvn org.sonarsource.scanner.maven:sonar-maven-plugin:5.1.0.4751:sonar \
    -Dsonar.projectKey="$PROJECT_NAME" \
    -Dsonar.host.url="$SONAR_URL" \
    -Dsonar.token="$SONAR_TOKEN" \
    -Dsonar.coverage.jacoco.xmlReportPaths=target/site/jacoco/jacoco.xml

echo ""
echo "5. SONARQUBE QUALITY GATE"
echo "--------------------------------------"

STATUS=$(curl -sS \
    -u "${SONAR_TOKEN}:" \
    "$SONAR_URL/api/qualitygates/project_status?projectKey=$PROJECT_NAME" \
    | python3 -c 'import sys,json; print(json.load(sys.stdin)["projectStatus"]["status"])')

echo "Quality Gate: $STATUS"

if [ "$STATUS" != "OK" ]; then
    echo "ERROR: SonarQube Quality Gate FAILED"
    exit 1
fi

echo ""
echo "6. DOCKER BUILD"
echo "--------------------------------------"

docker build -t "$IMAGE_NAME" .

echo ""
echo "7. START DOCKER CONTAINER"
echo "--------------------------------------"

# Remove old container if it exists
if docker ps -a --format '{{.Names}}' | grep -q "^${CONTAINER_NAME}$"; then
    echo "Removing existing container..."
    docker rm -f "$CONTAINER_NAME"
fi

docker run -d \
    --name "$CONTAINER_NAME" \
    -p "$APP_PORT:8080" \
    "$IMAGE_NAME"

echo "Waiting for application to start..."

for i in {1..30}; do
    if curl -s -o /dev/null -w "%{http_code}" \
        "http://localhost:${APP_PORT}/" | grep -q "200"; then
        echo "Application is UP"
        break
    fi

    if [ "$i" -eq 30 ]; then
        echo "ERROR: Application did not start"
        docker logs "$CONTAINER_NAME"
        exit 1
    fi

    sleep 2
done

echo ""
echo "8. E2E / SMOKE TESTS"
echo "--------------------------------------"

BASE_URL="http://localhost:${APP_PORT}"

check_endpoint() {
    URL="$1"

    echo "Testing: $URL"

    HTTP_CODE=$(curl -s -o /dev/null -w "%{http_code}" "$URL")

    if [ "$HTTP_CODE" = "200" ]; then
        echo "PASS: $URL -> HTTP $HTTP_CODE"
    else
        echo "FAIL: $URL -> HTTP $HTTP_CODE"
        docker logs "$CONTAINER_NAME"
        exit 1
    fi
}

check_endpoint "$BASE_URL/"
check_endpoint "$BASE_URL/owners/find"
check_endpoint "$BASE_URL/vets.html"

echo ""
echo "9. DOCKER STATUS"
echo "--------------------------------------"

docker ps --filter "name=$CONTAINER_NAME"

echo ""
echo "======================================"
echo " ALL DEVSECOPS CHECKS PASSED"
echo "======================================"

echo ""
echo "Gitleaks       : PASSED"
echo "Maven Tests    : PASSED"
echo "JaCoCo         : PASSED"
echo "SonarQube      : PASSED"
echo "Quality Gate   : PASSED"
echo "Docker Build   : PASSED"
echo "Docker Run     : PASSED"
echo "E2E Tests      : PASSED"
echo ""
echo "Application: http://localhost:${APP_PORT}"
echo "======================================"
