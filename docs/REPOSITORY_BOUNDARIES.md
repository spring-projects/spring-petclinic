# Repository boundaries

This fork is the application repository for a personal DevOps delivery lab. It retains the official Spring PetClinic source while adding application-adjacent delivery assets.

## Ownership model

| Repository | Owns |
|---|---|
| `spring-petclinic` | Application source, build and test assets, application container definition, and pipeline entry point |
| `petclinic-devops-automation` | Reusable infrastructure automation, configuration-management code, operational scripts, and Helm charts |
| `petclinic-environments` | Environment-specific desired configuration, deployment values, and promoted image references |

Application source and reusable automation do not belong in the environment repository. Environment-specific values and promotion state do not belong in the application repository.

## Official upstream

The `upstream` remote points to the official `spring-projects/spring-petclinic` repository and is fetch-only. The `origin` remote points to this fork.

Official changes are fetched from `upstream`, integrated on a short-lived synchronization branch, and reviewed through a pull request into this fork’s `main` branch. Changes are never pushed to the official repository from this lab.

## Delivery flow

1. Application changes are reviewed and built from this repository.
2. Reusable automation from the automation repository builds and deploys the platform.
3. Successful builds produce immutable application image references.
4. The environment repository records which image is promoted into each environment.

## Secret boundary

Credentials, private keys, local environment files, machine-specific application configuration, and generated runtime data must not enter Git. Public examples may contain documented placeholders but never usable secret values.