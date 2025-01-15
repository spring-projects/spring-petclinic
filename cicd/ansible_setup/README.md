# Ansible CI/CD Setup

This Ansible playbook provisions Jenkins, Nexus for a local CI/CD pipeline.

## How to Use

1. Install Ansible: `sudo apt install ansible`.
2. Run the playbook: `ansible-playbook -i inventory/local site.yml`.
3. Verify:
   - Jenkins: http://localhost:8080
   - Nexus: http://localhost:8081
