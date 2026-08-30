# Jonathan's Spring PetClinic Fork

A Jenkins pipeline (`Jenkinsfile`) and container build (`Dockerfile`) for Spring
PetClinic. All Maven dependencies resolve through JFrog Cloud Artifactory.

## Running the supplied image

The attached tarball is a linux/amd64 image (~100MB). It needs no JFrog access:

```bash
docker load -i spring-petclinic-<build-number>.tar
docker run --rm -p 8080:8080 spring-petclinic:latest
```

Open the application with <http://localhost:8080/>. Stop it with `Ctrl+C`.

> **On Apple Silicon**, Docker prints
> `WARNING: The requested image's platform (linux/amd64) does not match the
> detected host platform`. This is expected, not an error — the image runs under
> emulation, just slower to start.

## The pipeline

| Stage | What it does |
| --- | --- |
| Build | `./mvnw -B clean package -DskipTests` |
| Test | `./mvnw -B test` — 76 tests, including integration tests that start real databases |
| Package | Builds the image, tags it with the build number and `latest`, starts a container and polls `/actuator/health`, then exports it with `docker save` and archives the tarball |

The Package stage boots the image before passing, so an image that builds but
cannot actually start fails the build.

The Jenkins agent needs Docker with BuildKit, Docker Compose (the integration
tests start databases), and a `jfrog-cloud` credential — *Username with
password*, using a JFrog identity token as the password.

## Dependency resolution through JFrog

`.mvn/settings.xml` declares a mirror with `<mirrorOf>*</mirrorOf>`, so every
artifact — dependencies, build plugins and the Spring Boot parent POM — resolves
through JFrog rather than Maven Central. `.mvn/maven.config` applies those
settings to every `./mvnw` call, and the Docker build sets `MVNW_REPOURL` so the
Maven distribution itself also comes from JFrog. A full pipeline run resolved all
1,090 artifacts through JFrog and none from Maven Central.

No credentials are committed. `settings.xml` reads four environment variables:

| Variable | Meaning |
| --- | --- |
| `JFROG_URL` | Instance base URL, e.g. `https://trialp0kvcv.jfrog.io` |
| `JFROG_REPO` | Virtual Maven repository, e.g. `petclinic-libs-snapshot` |
| `JFROG_USER` | JFrog username or email |
| `JFROG_PASSWORD` | JFrog identity token |

In Jenkins the first two are set in the `Jenkinsfile` `environment` block. The
credentials come from the `jfrog-cloud` credential and reach the Docker build as
BuildKit `--secret` mounts, so they never land in an image layer.

## The image

A three-stage build producing a 256MB image that runs as a non-root user: the
first stage builds the jar and splits it into Spring Boot layers, the second uses
`jlink` to assemble a minimal Java runtime, and the third copies both onto
`gcr.io/distroless/base-debian12:nonroot`.

The runtime has no shell, so `docker exec ... sh` will not work.

## Building from source

A source build resolves through JFrog, so it needs credentials. No local JDK or
Maven is required — everything runs inside Docker.

```bash
export JFROG_USER=you@example.com
export JFROG_PASSWORD=<your JFrog identity token>

docker build \
  --platform linux/amd64 \
  --build-arg JFROG_URL=https://trialp0kvcv.jfrog.io \
  --build-arg JFROG_REPO=petclinic-libs-snapshot \
  --secret id=jfrog_user,env=JFROG_USER \
  --secret id=jfrog_password,env=JFROG_PASSWORD \
  -t spring-petclinic:latest .
```

## License

The Spring PetClinic sample application is released under version 2.0 of the [Apache License](https://www.apache.org/licenses/LICENSE-2.0).
