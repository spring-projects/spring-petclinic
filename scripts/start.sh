
#!/usr/bin/env bash
set -e

# Navigate to the project root
cd "$(dirname "$0")/.."

# Create the database volume if it does not exist
if ! docker volume inspect spring-petclinic_pgdata >/dev/null 2>&1; then
    docker volume create spring-petclinic_pgdata
fi

# Start the application and database
docker compose up -d --build
