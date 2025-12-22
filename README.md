# 🐾 Spring Petclinic Multi-Cloud GitOps (AWS EKS / GCP GKE)

[![Build Status](https://github.com/costRider/spring-petclinic/actions/workflows/ci-petclinic-eks.yaml/badge.svg)](https://github.com/costRider/spring-petclinic/actions/workflows/ci-petclinic-eks.yaml)
[![Build Status](https://github.com/costRider/spring-petclinic/actions/workflows/ci-petclinic-gke.yaml/badge.svg)](https://github.com/costRider/spring-petclinic/actions/workflows/ci-petclinic-gke.yaml)
---
본 프로젝트는 **Spring Petclinic 애플리케이션**을 대상으로  
**AWS(EKS)** 와 **GCP(GKE)** 두 CSP 환경에 동일한 서비스를 배포하고,  
**GitHub Actions + ArgoCD 기반 GitOps 방식**으로 CI/CD를 구성한 멀티클라우드 예제입니다.

각 CSP별로:
- 컨테이너 레지스트리
- Kubernetes 매니페스트
- Secret / 인증 방식
- CI 파이프라인

을 명확히 분리하여 운영하며, 실제 서비스 접속 및 **DB write 동작까지 검증 완료**된 상태입니다.

---

## 1. 전체 아키텍처 개요

```
GitHub Repository (spring-petclinic)
 ├─ GitHub Actions (CI)
 │   ├─ 공통: Maven Build & Test
 │   ├─ AWS CI: ECR 이미지 Push
 │   └─ GCP CI: GAR 이미지 Push
 │
 ├─ Container Registry
 │   ├─ AWS: Amazon ECR
 │   └─ GCP: Artifact Registry
 │
 ├─ ArgoCD (CD)
 │   ├─ AWS EKS Cluster
 │   └─ GCP GKE Cluster
 │
 └─ Kubernetes
     ├─ k8s/aws  (EKS 전용 매니페스트)
     └─ k8s/gcp  (GKE 전용 매니페스트)
```

---

## 2. Repository 구조

```
.github/workflows
 ├─ ci-petclinic-eks.yaml
 └─ ci-petclinic-gke.yaml

k8s/
 ├─ aws/
 └─ gcp/
```

---

## 3. GitHub Actions (CI)

### 공통
- Maven Build & Test
- Docker Image Build
- Commit SHA 기반 이미지 태그

### AWS (EKS)
- 인증: GitHub OIDC → AWS IAM Role
- Registry: Amazon ECR

### GCP (GKE)
- 인증: GitHub OIDC → GCP Workload Identity Federation
- Registry: Artifact Registry

---

## 4. Kubernetes 매니페스트

- AWS: `k8s/aws`
- GCP: `k8s/gcp`

CSP별 Ingress, Registry, Secret 차이로 매니페스트 분리

---

## 5. Secret / DB

- AWS: Secrets Manager + RDS (PostgreSQL)
- GCP: Secret Manager + Cloud SQL (PostgreSQL)
- External Secrets Operator 사용

---

## 6. ArgoCD

- AWS / GCP 각각 독립 ArgoCD
- GitHub Repo를 Source of Truth로 사용
- Git 변경 시 자동 배포

---

## 7. 검증 완료 항목

- CI 이미지 빌드 및 Push
- ArgoCD Sync
- 서비스 접속
- DB Read / Write 정상 동작

---

## 8. 정리

본 프로젝트는 멀티클라우드 환경에서 GitOps 기반 CI/CD와  
운영 분리를 어떻게 설계할 수 있는지를 보여주는 예제입니다.

---

## 9. Spring Boot 3.x 다운그레이드 메모 (Boot 4.0.0-M3 → Boot 3.2.5)

- 참고 문서: *Spring Boot 3 Migration Guide*, *Spring Boot 3.2 Release Notes*, *Spring Framework 6.x*.
- Jakarta 네임스페이스 전환 이후 API 의존성은 Boot 3.x 기본 BOM을 따라가며, Boot 4 / Spring 7 전용 의존성은 제거/하향 조정했다.
- Java 21 런타임을 유지하며, 모듈 경계 문제를 피하기 위해 `--add-opens=java.base/java.lang=ALL-UNNAMED` 옵션을 Whatap 활성화 시에만 주입한다.
- distroless/jlink 최적화는 별도 커밋으로 재적용 예정이며, 현재는 디버깅 및 호환성을 우선한 `eclipse-temurin:21-jre` 런타임을 사용한다.

### 잠재 호환 이슈 및 대응
- 서드파티 라이브러리의 Spring 7 전용 API 사용 → Boot 3.x BOM 기준으로 의존성 하향. 필요 시 BOM에 맞는 버전으로 재빌드.
- 에이전트/모듈 경계(`--add-opens`) 누락 시 JVM 시작 실패 가능 → Whatap 활성화 경로에서만 옵션을 주입하고, 기본 OFF 모드로 기동 보장.
- 컨테이너 디버깅(쉘 유무) → distroless 대신 temurin JRE를 기본값으로 사용, 추후 최적화는 별도 PR로 진행.

---

## 10. WhaTap APM 토글 사용법

- 기본값: OFF (`ENABLE_WHATAP=false`).
- 활성화: 컨테이너/Deployment 환경 변수로 아래를 설정한다.
  - `ENABLE_WHATAP=true`
  - `WHATAP_LICENSE`, `WHATAP_SERVER_HOST`, `WHATAP_MICRO_ENABLED`
- 이미지에 `/whatap/whatap.agent.jar`, `/whatap/paramkey.txt`가 포함되어 있으며, 활성화 시 엔트리포인트 래퍼가 자동으로 JVM 옵션을 주입한다.
- 외부 Secret(ExternalSecret: `whatap-apm-secret`)은 라이선스/서버/마이크로 트레이싱 키만 제공하며, `paramkey.txt`는 이미지에 포함된 파일을 사용한다.
