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
