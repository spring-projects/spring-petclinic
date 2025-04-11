# Advanced CI/CD Pipeline for Spring PetClinic

This repository demonstrates a comprehensive CI/CD pipeline for the Spring PetClinic application, showcasing DevOps best practices for enterprise applications.

## Architecture Overview

![CI/CD Pipeline Architecture](docs/images/cicd-architecture.png)

The CI/CD pipeline implements a fully automated workflow from code commit to production deployment, with the following components:

- **Source Control**: GitHub repository with branch protection rules
- **CI Pipeline**: GitHub Actions for building, testing, and scanning
- **Security Scanning**: SonarCloud, OWASP Dependency Check, Trivy, and ZAP
- **Infrastructure as Code**: Terraform for provisioning AWS resources
- **Containerization**: Docker for application packaging
- **Container Registry**: Amazon ECR for storing Docker images
- **Deployment Targets**: ECS clusters for dev, staging, and production environments
- **Performance Testing**: JMeter for load and performance testing
- **Approval Workflows**: Manual approvals for production deployments
- **Notifications**: Slack integration for deployment notifications

## Pipeline Workflow

1. **Code Commit**:
   - Developer pushes code to a feature branch
   - Pull request is created for review

2. **CI Process**:
   - Automated build and unit tests
   - Code quality and security scanning
   - Integration tests

3. **Artifact Creation**:
   - Docker image is built and tagged
   - Image is pushed to ECR repository

4. **Deployment Process**:
   - Infrastructure is provisioned/updated using Terraform
   - Application is deployed to the target environment
   - For production: manual approval is required

5. **Verification**:
   - Automated tests verify the deployment
   - Performance tests validate application under load

## Environment Setup

The pipeline manages three environments:

- **Development**: Automatically updated with each commit to main
- **Staging**: Used for pre-production testing
- **Production**: Protected by manual approval workflow

## Prerequisites

- GitHub account
- AWS account with appropriate permissions
- SonarCloud account
- Slack workspace (for notifications)

## Getting Started

### 1. Fork and Clone the Repository

```bash
git clone https://github.com/YOUR_USERNAME/spring-petclinic.git
cd spring-petclinic
```

### 2. Configure GitHub Secrets

The following secrets need to be configured in your GitHub repository:

- `AWS_ACCESS_KEY_ID`: AWS access key with permissions to create resources
- `AWS_SECRET_ACCESS_KEY`: Corresponding AWS secret key
- `SONAR_TOKEN`: SonarCloud authentication token
- `DB_USERNAME`: Database username for RDS instances
- `DB_PASSWORD`: Database password for RDS instances
- `VPC_ID`: AWS VPC ID where resources will be deployed
- `SLACK_WEBHOOK_URL`: Slack webhook URL for notifications

### 3. Set Up Infrastructure

Initialize the Terraform backend:

```bash
cd terraform
terraform init
```

### 4. Run the CI/CD Pipeline

The pipeline will automatically run on commits to the main branch. You can also manually trigger deployments from the GitHub Actions tab.

## Security Considerations

- Secrets are stored in GitHub Secrets and never exposed in logs
- Infrastructure follows security best practices with proper IAM roles
- Regular security scanning ensures vulnerabilities are identified early
- Production deployments require manual approval

## Monitoring and Observability

- Application metrics are collected using CloudWatch
- Logs are centralized for easy troubleshooting
- Alerts are configured for critical thresholds

## Extending the Pipeline

You can extend this pipeline by:

- Adding more thorough security scanning
- Implementing blue/green or canary deployments
- Adding chaos engineering tests
- Implementing infrastructure drift detection

## Contributing

Contributions are welcome! Please feel free to submit a Pull Request.

## License

This project is licensed under the MIT License - see the LICENSE file for details.