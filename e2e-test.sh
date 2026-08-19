#!/bin/bash

set -e

BASE_URL="http://localhost:8081"

echo "======================================"
echo " SPRING PETCLINIC E2E / SMOKE TEST"
echo "======================================"

check_endpoint() {
    URL="$1"

    echo "Testing: $URL"

    HTTP_CODE=$(curl -s -o /dev/null -w "%{http_code}" "$URL")

    if [ "$HTTP_CODE" = "200" ]; then
        echo "PASS: $URL → HTTP $HTTP_CODE"
    else
        echo "FAIL: $URL → HTTP $HTTP_CODE"
        exit 1
    fi
}

check_endpoint "$BASE_URL/"
check_endpoint "$BASE_URL/owners/find"
check_endpoint "$BASE_URL/vets.html"

echo
echo "======================================"
echo " ALL E2E TESTS PASSED"
echo "======================================"
