.SILENT: validate docker cluster
validate:
	test -n "$$EMAIL" || (echo "EMAIL is not set. Please set it first."; exit 1)
	if ! command which envsubst &> /dev/null; then \
		echo "gettext is not installed. Please install it first."; \
		exit 1; \
	fi
	if ! command which minikube &> /dev/null; then \
		echo "minikube is not installed. Please install it first."; \
		exit 1; \
	fi
	if ! command which kubectl &> /dev/null; then \
		echo "kubectl is not installed. Please install it first."; \
		exit 1; \
	fi
	if ! command which docker &> /dev/null; then \
		echo "docker is not installed. Please install it first."; \
		exit 1; \
	fi
	if ! command which md5sum &> /dev/null; then \
		echo "md5sum is not installed. Please install it first."; \
		exit 1; \
	fi
	if ! command which mvn &> /dev/null; then \
		echo "maven is not installed. Please install it first."; \
		exit 1; \
	fi

docker: validate
	if ! command docker info &> /dev/null; then \
		echo "docker is not running. Please make sure docker is running."; \
		exit 1; \
	fi

cluster: docker
	if ! command minikube status &> /dev/null; then \
		minikube config set memory 4096; \
		minikube start --driver=docker; \
	fi \

build: cluster
	mvn install
	minikube image build -t petclinic:latest .

deploy: build
	export RANDOM_PART=${EMAIL}_$$(hostname | md5sum | cut -c1-30) && \
	minikube kubectl -- apply -f kube/postgresql.yaml && \
	envsubst < kube/petclinic.yaml | minikube kubectl -- apply -f -

undeploy:
	minikube kubectl -- delete -f kube/