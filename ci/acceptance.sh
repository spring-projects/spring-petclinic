#!/usr/bin/env bash
# Teste de aceitação: sobe a aplicação e um Selenium em containers Docker,
# roda os testes *AT.java e derruba tudo ao final (passando ou falhando).
# Variáveis opcionais: BUILD_TAG (Jenkins), DOCKER_HOST, SELENIUM_HOST.
set -euo pipefail
export MSYS_NO_PATHCONV=1
cd "$(dirname "$0")/.."

SELENIUM_IMAGE="selenium/standalone-chrome:4.43.0-20260404"
WAIT_SECONDS=120

shopt -s nullglob
jars=(target/spring-petclinic-*.jar)
shopt -u nullglob
if [[ ${#jars[@]} -ne 1 ]]; then
	echo "ERRO: jar não encontrado em target/ (rode ./mvnw package)"
	exit 1
fi

id="${BUILD_TAG:-local-$$}"
id="$(printf '%s' "${id,,}" | tr -c 'a-z0-9.-' '-')"
network="petclinic-at-$id"
app="petclinic-at-app-$id"
selenium="petclinic-at-selenium-$id"
image="petclinic-at:$id"

# Uso: cleanup [código]. Com código (sinal), pula o log e remove tudo primeiro:
# o Jenkins manda SIGKILL poucos segundos depois do SIGTERM.
cleanup() {
	local code=$?
	local signaled=false
	if [[ $# -gt 0 ]]; then
		code=$1
		signaled=true
	fi
	trap - EXIT
	trap '' INT TERM
	if [[ $code -ne 0 && $signaled != true ]] && docker container inspect "$app" >/dev/null 2>&1; then
		echo "---- últimas 200 linhas do log da aplicação ----"
		docker logs --tail 200 "$app" || true
	fi
	docker rm -f "$app" "$selenium" >/dev/null 2>&1 || true
	docker network rm "$network" >/dev/null 2>&1 || true
	docker rmi "$image" >/dev/null 2>&1 || true
	exit "$code"
}
trap cleanup EXIT
trap 'cleanup 130' INT
trap 'cleanup 143' TERM

echo "Construindo a imagem $image"
docker build -f ci/acceptance.Dockerfile -t "$image" target

echo "Subindo a rede e os containers"
docker network create "$network" >/dev/null
docker run -d --name "$app" --network "$network" --network-alias petclinic "$image" >/dev/null
docker run -d --name "$selenium" --network "$network" --shm-size=2g -p 4444 "$SELENIUM_IMAGE" >/dev/null

if [[ -n "${SELENIUM_HOST:-}" ]]; then
	host="$SELENIUM_HOST"
elif [[ "${DOCKER_HOST:-}" == tcp://* ]]; then
	host="${DOCKER_HOST#tcp://}"
	host="${host%%[:/]*}"
else
	host="localhost"
fi
port_lines="$(docker port "$selenium" 4444/tcp)"
port="${port_lines%%$'\n'*}"
port="${port##*:}"
echo "Selenium em http://$host:$port"

# Esperas limitadas por relógio ($SECONDS), não por número de tentativas.
echo "Aguardando a aplicação (até $WAIT_SECONDS s)"
app_up=false
deadline=$((SECONDS + WAIT_SECONDS))
while ((SECONDS < deadline)); do
	if health="$(docker exec "$selenium" curl -fs --max-time 5 http://petclinic:8080/actuator/health 2>/dev/null)" 		&& [[ "$health" == *'"UP"'* ]]; then
		app_up=true
		break
	fi
	if [[ "$(docker container inspect -f '{{.State.Running}}' "$selenium" 2>/dev/null)" != true ]]; then
		echo "ERRO: Selenium não subiu em $WAIT_SECONDS s"
		exit 1
	fi
	sleep 2
done
if [[ "$app_up" != true ]]; then
	echo "ERRO: aplicação não subiu em $WAIT_SECONDS s"
	exit 1
fi

echo "Aguardando o Selenium (até $WAIT_SECONDS s)"
selenium_up=false
deadline=$((SECONDS + WAIT_SECONDS))
while ((SECONDS < deadline)); do
	if status="$(curl -fs --connect-timeout 5 --max-time 5 "http://$host:$port/status" 2>/dev/null)" 		&& [[ "$status" == *'"ready":true'* || "$status" == *'"ready": true'* ]]; then
		selenium_up=true
		break
	fi
	sleep 2
done
if [[ "$selenium_up" != true ]]; then
	echo "ERRO: Selenium não subiu em $WAIT_SECONDS s"
	exit 1
fi

echo "Rodando os testes de aceitação"
# MSYS_NO_PATHCONV quebra o classpath do mvnw no Git Bash; só os comandos docker precisam dele.
env -u MSYS_NO_PATHCONV ./mvnw -B --no-transfer-progress -Pacceptance \
	test-compile failsafe:integration-test@acceptance-tests failsafe:verify@acceptance-tests \
	"-Dacceptance.seleniumUrl=http://$host:$port" \
	"-Dacceptance.baseUrl=http://petclinic:8080"
