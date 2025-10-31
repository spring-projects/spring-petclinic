#!/bin/bash

# Jenkins Quick Start Script for Spring PetClinic
# This script helps you set up and configure Jenkins for the project

set -e

# Color codes for output
RED='\033[0;31m'
GREEN='\033[0;32m'
YELLOW='\033[1;33m'
BLUE='\033[0;34m'
NC='\033[0m' # No Color

# Function to print colored output
print_info() {
    echo -e "${BLUE}[INFO]${NC} $1"
}

print_success() {
    echo -e "${GREEN}[SUCCESS]${NC} $1"
}

print_warning() {
    echo -e "${YELLOW}[WARNING]${NC} $1"
}

print_error() {
    echo -e "${RED}[ERROR]${NC} $1"
}

# Function to check if a command exists
command_exists() {
    command -v "$1" >/dev/null 2>&1
}

# Check prerequisites
print_info "Checking prerequisites..."

if ! command_exists docker; then
    print_error "Docker is not installed. Please install Docker first."
    exit 1
fi

if ! command_exists docker-compose || ! command_exists "docker compose"; then
    print_error "Docker Compose is not installed. Please install Docker Compose first."
    exit 1
fi

print_success "Prerequisites check passed!"

# Start Jenkins
print_info "Starting Jenkins container..."
docker compose up -d jenkins

# Wait for Jenkins to start
print_info "Waiting for Jenkins to start (this may take a minute)..."
sleep 30

# Check if Jenkins is running
if ! docker ps | grep -q jenkins; then
    print_error "Jenkins container failed to start"
    exit 1
fi

print_success "Jenkins container is running!"

# Get initial admin password
print_info "Retrieving Jenkins initial admin password..."
INITIAL_PASSWORD=$(docker exec jenkins cat /var/jenkins_home/secrets/initialAdminPassword 2>/dev/null || echo "")

if [ -z "$INITIAL_PASSWORD" ]; then
    print_warning "Could not retrieve initial admin password. Jenkins might still be initializing."
    print_info "You can retrieve it later with: docker exec jenkins cat /var/jenkins_home/secrets/initialAdminPassword"
else
    print_success "Initial Admin Password: ${GREEN}${INITIAL_PASSWORD}${NC}"
    echo ""
    echo "=================================================="
    echo "IMPORTANT: Save this password!"
    echo "Initial Admin Password: ${INITIAL_PASSWORD}"
    echo "=================================================="
    echo ""
fi

# Display access information
echo ""
print_info "Jenkins Setup Information:"
echo "  - Jenkins URL: http://localhost:8080"
echo "  - Container Name: jenkins"
echo "  - Default Admin Password: See above"
echo ""

print_info "Next Steps:"
echo "  1. Open http://localhost:8080 in your browser"
echo "  2. Enter the initial admin password shown above"
echo "  3. Click 'Install suggested plugins'"
echo "  4. Create your first admin user"
echo "  5. Follow the setup guide in JENKINS_SETUP.md"
echo ""

print_info "Useful Commands:"
echo "  - View Jenkins logs: docker logs -f jenkins"
echo "  - Stop Jenkins: docker compose stop jenkins"
echo "  - Restart Jenkins: docker restart jenkins"
echo "  - Get admin password: docker exec jenkins cat /var/jenkins_home/secrets/initialAdminPassword"
echo ""

print_info "To set up the complete DevOps stack (Jenkins, SonarQube, Prometheus, Grafana, ZAP):"
echo "  docker compose up -d"
echo ""

print_success "Jenkins quick start completed!"
print_info "For detailed setup instructions, see JENKINS_SETUP.md"

