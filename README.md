# Practical task GHA for the intrnship GD

Original repositorium: https://github.com/spring-projects/spring-petclinic

## Prepare Docker Hub and geeral setups

For the purpose of solving this task I prepared two repositories in Docker Hub:

- main
- mr

In the GitHub Actions account I created several secrets to protect my code:

- DOCKERHUB_TOKEN - credentials for the DockerHub account
- DOCKER_USERNAME - credentials for the DockerHub account
- MAIN_REPO - "main" repo name
- MR_REPO - "mr" repo name


## Creating workflows

Under the `.github/workflows/` directory, two workflows where created:

- `build.yml` - The pipeline for a pull request, in the end it will push a Docker image into the "mr" repo
- `build_main.yml` - The pipeline for a push to the "main" branch, in the end it will push a Docker image into the "main" repo