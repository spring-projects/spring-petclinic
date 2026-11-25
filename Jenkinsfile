pipeline {
	agent any

	options {
		disableConcurrentBuilds()
		timestamps()
		timeout(time: 30, unit: 'MINUTES')
		buildDiscarder(logRotator(numToKeepStr: '20'))
	}

	triggers {
		githubPush()
		pollSCM('H/2 * * * *')
	}

	stages {
		stage('Build') {
			steps {
				sh './mvnw -B --no-transfer-progress -DskipTests package'
			}
		}

		stage('Testes de unidade') {
			steps {
				sh './mvnw -B --no-transfer-progress test'
			}
			post {
				always {
					junit testResults: 'target/surefire-reports/TEST-*.xml', allowEmptyResults: false
				}
			}
		}

		stage('Testes de integração') {
			steps {
				sh './mvnw -B --no-transfer-progress failsafe:integration-test failsafe:verify'
			}
			post {
				always {
					junit testResults: 'target/failsafe-reports/TEST-*.xml', allowEmptyResults: false
				}
			}
		}

		stage('Cobertura') {
			steps {
				sh './mvnw -B --no-transfer-progress jacoco:report'
				// Abaixo do mínimo o build é marcado como falho (changeBuildStatus).
				// O plugin limita o mínimo ao máximo (padrão 0), por isso os dois são informados.
				jacoco(
					execPattern: 'target/jacoco.exec',
					classPattern: 'target/classes',
					sourcePattern: 'src/main/java',
					minimumInstructionCoverage: '85',
					maximumInstructionCoverage: '85',
					minimumBranchCoverage: '75',
					maximumBranchCoverage: '75',
					minimumLineCoverage: '85',
					maximumLineCoverage: '85',
					changeBuildStatus: true
				)
				// O plugin só marca o build; aqui o estágio fica vermelho e os seguintes não rodam.
				script {
					if (currentBuild.currentResult == 'FAILURE') {
						error('Cobertura JaCoCo abaixo do mínimo configurado')
					}
				}
			}
		}

		stage('Teste de aceitação') {
			steps {
				sh 'test -x ci/acceptance.sh && ci/acceptance.sh'
			}
			post {
				always {
					// Sem relatório quando a aplicação não sobe; o erro real é o do script.
					junit testResults: 'target/acceptance-reports/TEST-*.xml', allowEmptyResults: true
					archiveArtifacts artifacts: 'target/acceptance-screenshots/*.png', allowEmptyArchive: true
				}
			}
		}

		stage('Lançamento') {
			when {
				buildingTag()
			}
			steps {
				withCredentials([usernamePassword(credentialsId: 'dockerhub-petclinic',
					usernameVariable: 'DOCKERHUB_USER', passwordVariable: 'DOCKERHUB_TOKEN')]) {
					sh '''
						set -eu
						tag="${TAG_NAME}"
						printf '%s' "$tag" | grep -Eq '^v[0-9]+\\.[0-9]+\\.[0-9]+$'
						tag="${tag#v}"
						test -f Dockerfile
						test -x ci/deploy.sh
						docker build --tag "${DOCKERHUB_USER}/petclinic-cicd:${tag}" .
						printf '%s' "$DOCKERHUB_TOKEN" | docker login --username "$DOCKERHUB_USER" --password-stdin
						docker push "${DOCKERHUB_USER}/petclinic-cicd:${tag}"
						docker tag "${DOCKERHUB_USER}/petclinic-cicd:${tag}" "${DOCKERHUB_USER}/petclinic-cicd:latest"
						docker push "${DOCKERHUB_USER}/petclinic-cicd:latest"
						ci/deploy.sh "${DOCKERHUB_USER}/petclinic-cicd:${tag}"
					'''
				}
			}
			post {
				always {
					sh 'docker logout || true'
				}
			}
		}
	}

	post {
		always {
			cleanWs()
		}
	}
}
