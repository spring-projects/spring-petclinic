---
    name: Build to Docker Hub
    
    on:
      push:
        branches: [master]
    
    jobs:
      build:
        runs-on: ubuntu-latest
        steps:
          - name: Checkout the repository
            uses: actions/checkout@v4
          - name: Set up Docker Buildx
            uses: docker/setup-buildx-action@v3
          - name: Login to Docker Hub
            uses: docker/login-action@v3
            with:
              username: ${{ secrets.DOCKERHUB_USERNAME }}
              password: ${{ secrets.DOCKERHUB_TOKEN }}
          - name: Set up JDK 17
            uses: actions/setup-java@v3
            with:
              java-version: '17'
              distribution: 'temurin'
              cache: maven
          - name: Build & publish the project
            run: mvn compile jib:build -X -DjibSerialize=true