# DevOps Pipeline Setup Guide

Complete guide for setting up the DevOps pipeline with Jenkins, SonarQube, Prometheus, Grafana, and OWASP ZAP.

## Prerequisites

- Docker and Docker Compose installed
- Git installed
- GitHub account
- Java 25 installed locally (for local testing)

All services run on custom Docker network: `devops-net`

## Step-by-Step Setup

### 1. Fork and Clone Repository

```bash
# Fork the repository on GitHub
# Then clone your fork
git clone https://github.com/YOUR_USERNAME/spring-petclinic.git
cd spring-petclinic
```

### 2. Build Custom Maven-Java25 Image

```bash
docker compose build maven-java25
```

### 3. Start All Services

```bash
docker compose up -d
```

This starts:

- **Jenkins** on `http://localhost:8082/jenkins`
- **SonarQube** on `http://localhost:9000`
- **Prometheus** on `http://localhost:9090`
- **Grafana** on `http://localhost:3030`
- **OWASP ZAP** on `http://localhost:8081`

### 4. Configure SonarQube

```bash
# Wait for SonarQube to start (check logs)
docker logs -f sonarqube

# Once ready, open http://localhost:9000
# Login: admin / admin (change password on first login)
```

**Generate Token:**

1. Click on your avatar → My Account
2. Security tab → Generate Tokens
3. Name: `jenkins-token`
4. Type: Global Analysis Token
5. Copy the generated token

### 5. Configure Jenkins

```bash
# Get initial admin password
docker exec jenkins cat /var/jenkins_home/secrets/initialAdminPassword

# Open http://localhost:8082/jenkins
# Paste the password and click Continue
# Select "Install suggested plugins"
# Create admin user
```

**Add SonarQube Server:**

1. Manage Jenkins → Configure System
2. Scroll to "SonarQube servers"
3. Click "Add SonarQube"
   - Name: `SonarQubeServer`
   - Server URL: `http://sonarqube:9000`
   - Server authentication token: Add → Jenkins → Secret text
     - Secret: (paste your SonarQube token)
     - ID: `sonar-token`
     - Description: `SonarQube Token`
4. Save

### 6. Create Jenkins Pipeline

**Option A: Multibranch Pipeline (Recommended)**

1. New Item → Multibranch Pipeline
2. Name: `petclinic-spring`
3. Branch Sources → Add source → Git
   - Project Repository: `https://github.com/YOUR_USERNAME/spring-petclinic`
4. Build Configuration → Mode: by Jenkinsfile
5. Scan Multibranch Pipeline Triggers:
   - ✓ Periodically if not otherwise run
   - Interval: 1 minute
6. Save

**Option B: Pipeline**

1. New Item → Pipeline
2. Name: `petclinic-spring`
3. Build Triggers:
   - ✓ Poll SCM
   - Schedule: `* * * * *` (every minute)
4. Pipeline:
   - Definition: Pipeline script from SCM
   - SCM: Git
   - Repository URL: `https://github.com/YOUR_USERNAME/spring-petclinic`
   - Branch: `*/main` (or your branch)
5. Save

### 7. View in Blue Ocean

1. Click "Open Blue Ocean" in Jenkins sidebar
2. Select your pipeline
3. Click "Run" to trigger a build
4. Watch the visual pipeline execution

### 8. Configure Prometheus Monitoring

**Verify Prometheus is scraping Jenkins:**

```bash
# Open http://localhost:9090
# Go to Status → Targets
# Verify jenkins endpoint is UP
```

**Prometheus Configuration** (`monitoring/prometheus.yml`):

```yaml
scrape_configs:
  - job_name: "jenkins"
    metrics_path: "/jenkins/prometheus"
    static_configs:
      - targets: ["jenkins:8080"]
```

### 9. Configure Grafana Dashboards

```bash
# Open http://localhost:3030
# Login: admin / admin
```

**Add Prometheus Data Source:**

1. Configuration → Data Sources
2. Add data source → Prometheus
3. URL: `http://prometheus:9090`
4. Save & Test

**Import Jenkins Dashboard:**

1. Create → Import
2. Dashboard ID: `9964` (Jenkins: Performance and Health Overview)
3. Select Prometheus data source
4. Import

## Service URLs

| Service    | URL                           | Credentials |
| ---------- | ----------------------------- | ----------- |
| Jenkins    | http://localhost:8082/jenkins | admin/admin |
| SonarQube  | http://localhost:9000         | admin/admin |
| Prometheus | http://localhost:9090         | -           |
| Grafana    | http://localhost:3030         | admin/admin |
| ZAP        | http://localhost:8081         | -           |

## Clean Up

```bash
# Stop all services
docker compose down

# Remove volumes (WARNING: deletes all data)
docker compose down -v
```
